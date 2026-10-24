package com.example.migrations.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // column renamed in migration 2 to 3
    @ColumnInfo(name = "data_label")
    val label: String,
    // new column
    @ColumnInfo(defaultValue = "${0L}", name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)