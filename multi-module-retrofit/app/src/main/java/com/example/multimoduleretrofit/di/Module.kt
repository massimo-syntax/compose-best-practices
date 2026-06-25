package com.example.multimoduleretrofit.di

import com.example.data.repository.ItemsRepositoryImpl
import com.example.domain.repository.ItemsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyRepository(): ItemsRepository {
        return ItemsRepositoryImpl()
    }
}