package com.example.roomrelationsmigrations.data.onetoone

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface OneToOneDao {

    @Query("SELECT * FROM persons ORDER BY id ASC")
    fun getPersons(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM profiles ORDER BY id ASC")
    fun getProfiles(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersons(persons: List<PersonEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<ProfileEntity>)

    @Query("DELETE FROM persons")
    suspend fun deleteAllPersons()

    @Query("DELETE FROM profiles")
    suspend fun deleteAllProfiles()

    @Transaction
    suspend fun deleteAllData() {
        deleteAllPersons()
        deleteAllProfiles()
    }

    // get persons with their profiles from the relation defined in PersonProfileEntity
    @Query("SELECT * FROM persons")
    fun getPersonsWithProfiles(): Flow<List<PersonProfileEntity>>

}
