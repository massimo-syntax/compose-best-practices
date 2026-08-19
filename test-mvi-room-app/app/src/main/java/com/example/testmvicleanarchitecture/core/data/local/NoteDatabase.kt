package com.example.testmvicleanarchitecture.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testmvicleanarchitecture.data.local.dao.NoteDao
import com.example.testmvicleanarchitecture.data.local.entity.NoteEntity


@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}
