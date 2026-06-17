package com.vtol.petpal.data.repository

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.vtol.petpal.domain.repository.AnalyticsHelper
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    private val analytics: FirebaseAnalytics
) : AnalyticsHelper {

    override fun logEvent(event: String, params: Bundle?) {
        analytics.logEvent(event, params)
    }

    override fun logScreenView(screenName: String) {
        analytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf(
                FirebaseAnalytics.Param.SCREEN_NAME to screenName
            )
        )
    }
}