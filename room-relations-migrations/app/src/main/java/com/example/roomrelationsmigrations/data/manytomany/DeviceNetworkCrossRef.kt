package com.example.roomrelationsmigrations.data.manytomany

import androidx.room.Entity

@Entity(
    tableName = "device_network_cross_ref",
    primaryKeys = ["deviceId", "networkId"],
)
data class DeviceNetworkCrossRef(
    val deviceId: Long,
    val networkId: Long,
)
