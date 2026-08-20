package com.example.testmvicleanarchitecture.core.di

import android.content.Context
import androidx.room.Room
import com.example.testmvicleanarchitecture.core.data.local.NoteDatabase
import com.example.testmvicleanarchitecture.core.data.local.dao.NoteDao
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
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase {
        return Room.databaseBuilder(
                context,
                NoteDatabase::class.java,
                NoteDatabase.DATABASE_NAME
            ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao
    }
}
