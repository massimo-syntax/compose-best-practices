package com.example.querymigrations.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String,
    // new column
    @ColumnInfo(defaultValue = "${0L}", name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)