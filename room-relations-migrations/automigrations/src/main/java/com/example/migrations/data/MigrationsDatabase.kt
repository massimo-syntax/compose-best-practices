package com.example.migrations.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [DataEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = MigrationsDatabase.RenameColumnLabelToDataLabel::class)
    ]
)
abstract class MigrationsDatabase : RoomDatabase(){
    abstract fun getDataDao(): DataDao
    @RenameColumn(tableName = "data_table", fromColumnName = "label", toColumnName = "data_label")
    class RenameColumnLabelToDataLabel : AutoMigrationSpec
}

