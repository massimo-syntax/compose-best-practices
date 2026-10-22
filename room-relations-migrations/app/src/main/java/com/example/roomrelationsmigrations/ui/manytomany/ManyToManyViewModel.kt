package com.example.roomrelationsmigrations.ui.manytomany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomrelationsmigrations.data.manytomany.DeviceEntity
import com.example.roomrelationsmigrations.data.manytomany.DeviceNetworkCrossRef
import com.example.roomrelationsmigrations.data.manytomany.DeviceWithNetworks
import com.example.roomrelationsmigrations.data.manytomany.ManyToManyDao
import com.example.roomrelationsmigrations.data.manytomany.NetworkEntity
import com.example.roomrelationsmigrations.data.manytomany.NetworkWithDevices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ManyToManyUiState(
    val devices: List<DeviceEntity> = emptyList(),
    val networks: List<NetworkEntity> = emptyList(),
    val devicesWithNetworks: List<DeviceWithNetworks> = emptyList(),
    val networksWithDevices: List<NetworkWithDevices> = emptyList(),
)

@HiltViewModel
class ManyToManyViewModel @Inject constructor(
    private val dao: ManyToManyDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManyToManyUiState())
    val uiState: StateFlow<ManyToManyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                dao.getDevices(),
                dao.getNetworks(),
                dao.getDevicesWithNetworks(),
                dao.getNetworksWithDevices()
            ) { devices, networks, devicesWithNetworks, networkWithDevices ->
                ManyToManyUiState(devices, networks, devicesWithNetworks, networkWithDevices)
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun addRandomEntries() {
        val deviceNames = listOf("Smartphone", "Laptop", "Smart TV")
        val networkNames = listOf("Home Wi-Fi 5G", "Office Network")

        val newDevices = deviceNames.map { DeviceEntity(name = it) }
        val newNetworks = networkNames.map { NetworkEntity(name = it) }

        viewModelScope.launch {
            val deviceIds = dao.insertDevices(newDevices)
            val networkIds = dao.insertNetworks(newNetworks)

                val crossRefs = listOf(
                    DeviceNetworkCrossRef(deviceId = deviceIds[0], networkId = networkIds[0]),
                    DeviceNetworkCrossRef(deviceId = deviceIds[0], networkId = networkIds[1]),
                    DeviceNetworkCrossRef(deviceId = deviceIds[1], networkId = networkIds[1]),
                    DeviceNetworkCrossRef(deviceId = deviceIds[2], networkId = networkIds[0]),
                )
                dao.insertDeviceNetworkCrossRefs(crossRefs)
            }
    }

    fun deleteAllEntries() {
        viewModelScope.launch {
            dao.deleteAllData()
        }
    }
}
