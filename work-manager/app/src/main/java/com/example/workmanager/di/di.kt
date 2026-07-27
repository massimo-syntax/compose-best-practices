package com.example.workmanager

import android.content.Context
import androidx.work.WorkManager
import com.example.workmanager.workmanager.WorkManagerHandler
import com.example.workmanager.workmanager.WorkManagerHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {
    @Provides
    @Singleton
    fun provideWorkManagerInstance(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)
}

// work manager handler has the WorkManager as parameter
@Module
@InstallIn(SingletonComponent::class)
abstract class WorkManagerBindingsModule {

    @Binds
    @Singleton
    abstract fun bindWorkManagerHandler(
        impl: WorkManagerHandlerImpl
    ): WorkManagerHandler
}

