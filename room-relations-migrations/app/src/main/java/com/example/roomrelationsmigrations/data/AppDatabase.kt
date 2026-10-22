package com.example.roomrelationsmigrations.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomrelationsmigrations.data.manytomany.DeviceEntity
import com.example.roomrelationsmigrations.data.manytomany.DeviceNetworkCrossRef
import com.example.roomrelationsmigrations.data.manytomany.ManyToManyDao
import com.example.roomrelationsmigrations.data.manytomany.NetworkEntity
import com.example.roomrelationsmigrations.data.onetomany.AppEntity
import com.example.roomrelationsmigrations.data.onetomany.OneToManyDao
import com.example.roomrelationsmigrations.data.onetomany.UserEntity
import com.example.roomrelationsmigrations.data.onetoone.OneToOneDao
import com.example.roomrelationsmigrations.data.onetoone.PersonEntity
import com.example.roomrelationsmigrations.data.onetoone.ProfileEntity
import com.example.roomrelationsmigrations.data.recordstandard.EmployeeDao
import com.example.roomrelationsmigrations.data.recordstandard.EmployeeEntity

@Database(
    entities = [
        EmployeeEntity::class,
        PersonEntity::class,
        ProfileEntity::class,
        AppEntity::class,
        UserEntity::class,
        DeviceEntity::class,
        NetworkEntity::class,
        DeviceNetworkCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun oneToOneDao(): OneToOneDao
    abstract fun oneToManyDao(): OneToManyDao
    abstract fun manyToManyDao(): ManyToManyDao
}
