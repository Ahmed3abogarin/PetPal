package com.vtol.petpal.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.queryProductDetails
import com.vtol.petpal.data.repository.PremiumRepositoryImpl
import com.vtol.petpal.presentation.premium.PremiumPlan
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class BillingManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val premiumRepository: PremiumRepositoryImpl
) {
    private val billingClient = BillingClient.newBuilder(context)
        .setListener { billingResult, purchases ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    purchases?.forEach { handlePurchase(it) }
                }

                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    Timber.d("User cancelled purchase")
                }

                else -> {
                    // v9: check sub-response code for more detail
                    Timber.e("Purchase failed: ${billingResult.debugMessage}")
                }
            }
        }
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .enablePrepaidPlans()
                .build()
        )
        .build()

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Timber.d("Billing connected")
                    queryExistingPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                // v8+: auto-reconnects, but you can retry manually if needed
                Timber.w("Billing disconnected")
            }
        })
    }

    suspend fun launchPurchaseFlow(activity: Activity, plan: PremiumPlan): Result<Unit> {
        return try {
            if (!billingClient.isReady) {
                val connected = connectIfNeeded()
                if (!connected) return Result.failure(Exception("Billing service unavailable"))
            }

            val productId = when (plan) {
                PremiumPlan.MONTHLY -> "premium_monthly"
                PremiumPlan.YEARLY -> "premium_yearly"
            }

            val productType = when (plan) {
                PremiumPlan.MONTHLY, PremiumPlan.YEARLY -> BillingClient.ProductType.SUBS
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(productType)
                            .build()
                    )
                ).build()

            // 1. Suspend and wait for the query result instead of using a callback
            val queryResult = billingClient.queryProductDetails(params)
            val billingResult = queryResult.billingResult

            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                val msg = "Query failed: ${billingResult.debugMessage}"
                Timber.e(msg)
                return Result.failure(Exception(msg))
            }

            val productDetails = queryResult.productDetailsList?.firstOrNull() ?: run {
                val msg = "No product found: $productId"
                Timber.e(msg)
                return Result.failure(Exception(msg))
            }

            val productDetailsParams = when (plan) {
                PremiumPlan.MONTHLY, PremiumPlan.YEARLY -> {
                    val offerDetails = productDetails.subscriptionOfferDetails

                    if (offerDetails.isNullOrEmpty()) {
                        val msg = "No offer details found for $productId"
                        Timber.e(msg)
                        return Result.failure(Exception(msg))
                    }

                    val offerToken = offerDetails.first().offerToken

                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .setOfferToken(offerToken)
                            .build()
                    )
                }
            }

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParams)
                .build()

            // 2. Switch to the Main thread to launch the billing flow
            withContext(Dispatchers.Main) {
                val flowResult = billingClient.launchBillingFlow(activity, billingFlowParams)
                if (flowResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    val msg = "launchBillingFlow failed: ${flowResult.debugMessage}"
                    Timber.e(msg)
                    Result.failure(Exception(msg))
                } else {
                    Result.success(Unit)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "launchPurchaseFlow error: ${e.message}")
            Result.failure(e)
        }
    }
    private fun queryExistingPurchases() {
        // check subscriptions
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { _, purchases ->
            val hasActiveSub = purchases.any {
                it.purchaseState == Purchase.PurchaseState.PURCHASED
            }
            if (hasActiveSub) {
                CoroutineScope(Dispatchers.IO).launch {
                    premiumRepository.setPremium(true)
                }
                return@queryPurchasesAsync
            }

            // check one-time purchases (lifetime)
            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            ) { _, inAppPurchases ->
                val hasLifetime = inAppPurchases.any {
                    it.purchaseState == Purchase.PurchaseState.PURCHASED
                }
                CoroutineScope(Dispatchers.IO).launch {
                    premiumRepository.setPremium(hasLifetime)
                }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(params) { result ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Timber.d("Purchase acknowledged")
                    CoroutineScope(Dispatchers.IO).launch {
                        premiumRepository.setPremium(true)
                    }
                }
            }
        }
    }
    private suspend fun connectIfNeeded(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    continuation.resume(result.responseCode == BillingClient.BillingResponseCode.OK)
                }

                override fun onBillingServiceDisconnected() {
                    continuation.resume(false)
                }
            })
        }
    }
}