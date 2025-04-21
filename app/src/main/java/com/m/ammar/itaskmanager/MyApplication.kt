package com.m.ammar.itaskmanager

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * MyApplication class initializes global configurations for the app.
 * It sets up Timber logging in debug mode for better log management.
 */
@HiltAndroidApp
class MyApplication : Application() {

    /**
     * Called when the application is created.
     * Initializes Timber logging in debug builds.
     */
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    /**
     * Initializes Timber for logging. It uses Timber.DebugTree for debug builds
     * and logs a message based on the build type.
     */
    private fun initTimber() {
        // Initialize Timber DebugTree for logging in debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())  // Plant the debug tree for logging
            Log.e("TIMBER", "Timber Start")   // Log that Timber is started
        } else {
            Log.e("TIMBER", "Timber Stop")    // Log that Timber is stopped
        }
    }
}

