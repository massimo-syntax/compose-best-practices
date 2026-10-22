package com.example.roomrelationsmigrations.data.manytomany

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "networks")
data class NetworkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
)
