package com.example.migrations.di

import android.content.Context
import androidx.room.Room
import com.example.migrations.data.DataDao
import com.example.migrations.data.MigrationsDatabase
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
    ): MigrationsDatabase {
        return Room.databaseBuilder(
            context,
            MigrationsDatabase::class.java,
            "migrations_database"
        )
            //.fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideDataDao(database: MigrationsDatabase): DataDao {
        return database.getDataDao()
    }

}