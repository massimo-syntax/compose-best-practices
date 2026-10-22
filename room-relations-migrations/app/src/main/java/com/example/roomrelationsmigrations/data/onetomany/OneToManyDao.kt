package com.example.roomrelationsmigrations.data.onetomany

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface OneToManyDao {

    @Query("SELECT * FROM apps ORDER BY id ASC")
    fun getApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): Flow<List<UserEntity>>

    @Transaction
    @Query("SELECT * FROM apps")
    fun getAppsWithUsers(): Flow<List<AppWithUsers>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<AppEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("DELETE FROM apps")
    suspend fun deleteAllApps()

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Transaction
    suspend fun deleteAllData() {
        deleteAllApps()
        deleteAllUsers()
    }
}
