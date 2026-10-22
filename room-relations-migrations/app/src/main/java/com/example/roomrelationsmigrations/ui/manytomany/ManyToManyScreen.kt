package com.example.roomrelationsmigrations.ui.manytomany

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roomrelationsmigrations.data.manytomany.DeviceEntity
import com.example.roomrelationsmigrations.data.manytomany.DeviceWithNetworks
import com.example.roomrelationsmigrations.data.manytomany.NetworkEntity
import com.example.roomrelationsmigrations.data.manytomany.NetworkWithDevices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManyToManyScreen(
    viewModel: ManyToManyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isDatabaseEmpty = uiState.devices.isEmpty() && uiState.networks.isEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Many To Many (Device - Networks)") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.addRandomEntries() },
                    enabled = isDatabaseEmpty,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add 3 Devices & 2 Networks")
                }

                OutlinedButton(
                    onClick = { viewModel.deleteAllEntries() },
                    enabled = !isDatabaseEmpty,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Delete All")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Devices (${uiState.devices.size})",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.devices.isEmpty()) {
                Text(
                    text = "No devices yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(items = uiState.devices, key = { it.id }) { device ->
                        DeviceCard(device = device)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Networks (${uiState.networks.size})",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.networks.isEmpty()) {
                Text(
                    text = "No networks yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(items = uiState.networks, key = { it.id }) { network ->
                        NetworkCard(network = network)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Devices with Networks & Networks with Devices",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.devicesWithNetworks.isEmpty() && uiState.networksWithDevices.isEmpty()) {
                Text(
                    text = "No relations loaded yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(
                        items = uiState.devicesWithNetworks,
                        key = { "device_${it.device.id}" }
                    ) { deviceWithNetworks ->
                        DeviceWithNetworksCard(deviceWithNetworks = deviceWithNetworks)
                    }

                    items(
                        items = uiState.networksWithDevices,
                        key = { "network_${it.network.id}" }
                    ) { networkWithDevices ->
                        NetworkWithDevicesCard(networkWithDevices = networkWithDevices)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: DeviceEntity) {
    Card(
        modifier = Modifier.width(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ID: ${device.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NetworkCard(network: NetworkEntity) {
    Card(
        modifier = Modifier.width(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = network.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ID: ${network.id}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DeviceWithNetworksCard(deviceWithNetworks: DeviceWithNetworks) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Device: ${deviceWithNetworks.device.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Connected Networks (${deviceWithNetworks.networks.size}):",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (deviceWithNetworks.networks.isEmpty()) {
                Text(
                    text = "• None",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                deviceWithNetworks.networks.forEach { network ->
                    Text(
                        text = "• ${network.name} (ID: ${network.id})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun NetworkWithDevicesCard(networkWithDevices: NetworkWithDevices) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Network: ${networkWithDevices.network.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Connected Devices (${networkWithDevices.devices.size}):",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (networkWithDevices.devices.isEmpty()) {
                Text(
                    text = "• None",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                networkWithDevices.devices.forEach { device ->
                    Text(
                        text = "• ${device.name} (ID: ${device.id})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
