package com.example.roomrelationsmigrations.data.manytomany

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DeviceWithNetworks(
    @Embedded val device: DeviceEntity,
    @Relation(
        // its column: id of -> network
        parentColumn = "id",
        // id of related entity, -> device
        entityColumn = "id",
        associateBy = Junction(
            value = DeviceNetworkCrossRef::class,
            parentColumn = "deviceId",
            entityColumn = "networkId",
        ),
    )
    val networks: List<NetworkEntity>,
)

data class NetworkWithDevices(
    @Embedded val network: NetworkEntity,
    @Relation(
        // its column: id of -> network
        parentColumn = "id",
        // id of related entity, -> device
        entityColumn = "id",
        associateBy = Junction(
            value = DeviceNetworkCrossRef::class,
            parentColumn = "networkId",
            entityColumn = "deviceId",
        ),
    )
    val devices: List<DeviceEntity>,
)
