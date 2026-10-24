package com.example.querymigrations.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// migration from version 1 to 2
val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE data_table ADD COLUMN created_at INTEGER NOT NULL DEFAULT 0")
    }
}