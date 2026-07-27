package com.example.workmanager


import android.app.Application
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.Configuration
import androidx.work.WorkManager;

import dagger.hilt.android.HiltAndroidApp;
import jakarta.inject.Inject;

@HiltAndroidApp
class HiltWorkMangerApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory:
            HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this, workManagerConfiguration)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}