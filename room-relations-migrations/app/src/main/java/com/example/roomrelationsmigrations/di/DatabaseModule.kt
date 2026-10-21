package com.example.roomrelationsmigrations.di

import android.content.Context
import androidx.room.Room
import com.example.roomrelationsmigrations.data.AppDatabase
import com.example.roomrelationsmigrations.data.recordstandard.EmployeeDao
import com.example.roomrelationsmigrations.data.onetoone.OneToOneDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "local_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideEmployeeDao(database: AppDatabase): EmployeeDao {
        return database.employeeDao()
    }

    @Provides
    fun provideOneToOneDao(database: AppDatabase): OneToOneDao {
        return database.oneToOneDao()
    }
}
