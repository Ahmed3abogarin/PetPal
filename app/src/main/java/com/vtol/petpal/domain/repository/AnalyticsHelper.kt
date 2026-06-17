package com.vtol.petpal.domain.repository

import android.os.Bundle

interface AnalyticsHelper {
    fun logEvent(event: String, params: Bundle? = null)

    fun logScreenView(screenName: String)
}