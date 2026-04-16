package com.vtol.petpal

import android.app.Application
import com.vtol.petpal.data.notification.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application: Application(){
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        NotificationHelper.createChannel(this)
    }
}