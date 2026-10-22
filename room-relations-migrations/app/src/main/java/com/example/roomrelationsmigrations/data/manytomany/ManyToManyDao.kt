package com.example.roomrelationsmigrations.data.manytomany

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ManyToManyDao {

    // device
    @Query("SELECT * FROM devices ORDER BY id ASC")
    fun getDevices(): Flow<List<DeviceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<DeviceEntity>): List<Long>

    @Query("DELETE FROM devices")
    suspend fun deleteAllDevices()

    @Transaction
    @Query("SELECT * FROM devices")
    fun getDevicesWithNetworks(): Flow<List<DeviceWithNetworks>>


    // network
    @Query("SELECT * FROM networks ORDER BY id ASC")
    fun getNetworks(): Flow<List<NetworkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNetworks(networks: List<NetworkEntity>): List<Long>

    @Transaction
    @Query("SELECT * FROM networks")
    fun getNetworksWithDevices(): Flow<List<NetworkWithDevices>>


    // cross ref
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceNetworkCrossRefs(crossRefs: List<DeviceNetworkCrossRef>)

    @Query("DELETE FROM device_network_cross_ref")
    suspend fun deleteAllCrossRefs()

    @Query("DELETE FROM networks")
    suspend fun deleteAllNetworks()

    @Transaction
    suspend fun deleteAllData() {
        deleteAllDevices()
        deleteAllNetworks()
        deleteAllCrossRefs()
    }
}
