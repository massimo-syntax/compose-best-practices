package com.example.roomrelationsmigrations.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomrelationsmigrations.data.onetoone.OneToOneDao
import com.example.roomrelationsmigrations.data.onetoone.PersonEntity
import com.example.roomrelationsmigrations.data.onetoone.ProfileEntity
import com.example.roomrelationsmigrations.data.recordstandard.EmployeeDao
import com.example.roomrelationsmigrations.data.recordstandard.EmployeeEntity

@Database(
    entities = [
        EmployeeEntity::class,
        PersonEntity::class,
        ProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun oneToOneDao(): OneToOneDao
}
