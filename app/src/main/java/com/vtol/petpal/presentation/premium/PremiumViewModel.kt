package com.vtol.petpal.presentation.premium

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.billing.BillingManager
import com.vtol.petpal.domain.repository.PremiumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PremiumUiState(
    val selectedPlan: PremiumPlan = PremiumPlan.YEARLY,
    val isPurchasing: Boolean = false,
    val isPremium: Boolean = false,
    val error: String? = null
)

enum class PremiumPlan { MONTHLY, YEARLY } // LIFETIME

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val billingManager: BillingManager,
    private val premiumRepository: PremiumRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PremiumUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            premiumRepository.isPremium().collect { isPremium ->
                _state.update { it.copy(isPremium = isPremium) }
            }
        }
    }

    fun onEvent(event: PremiumEvent){
        when(event){
            is PremiumEvent.PlanSelected -> onPlanSelected(event.plan)
            is PremiumEvent.PurchaseClicked -> onPurchaseClicked(event.activity)
        }
    }
    private fun onPlanSelected(plan: PremiumPlan) {
        _state.update { it.copy(selectedPlan = plan) }
    }

    private fun onPurchaseClicked(activity: Activity) {
        viewModelScope.launch {
            _state.update { it.copy(isPurchasing = true, error = null) }

            billingManager.launchPurchaseFlow(activity, _state.value.selectedPlan)
                .onSuccess {
                    _state.update { it.copy(isPurchasing = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isPurchasing = false, error = e.message) }
                }
        }
    }
}