package com.example.querymigrations.di

import android.content.Context
import androidx.room.Room
import com.example.querymigrations.data.DataDao
import com.example.querymigrations.data.MigrationsDatabase
import com.example.querymigrations.data.migrations.MIGRATION_1_2
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
            "query_migrations_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideDataDao(database: MigrationsDatabase): DataDao {
        return database.getDataDao()
    }

}