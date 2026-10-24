package com.example.querymigrations.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [DataEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class MigrationsDatabase : RoomDatabase(){
    abstract fun getDataDao(): DataDao

}

