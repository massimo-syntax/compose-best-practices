package com.example.simplestdatastore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen(
    username: String = "nothing, no name here",
    viewModel: SettingsViewModel = viewModel(factory = myViewmodelFactory),
    onUpdateTheme: (SelectedTheme) -> Unit
) {
    val names = listOf("name_1", "name_2", "name_3", "name_4")
    val userPreferences by viewModel.userPreferences.collectAsStateWithLifecycle()
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (userPreferences.loading)
        // from main activity
            Text("Selected Name: $username")
        else
            Text("Selected Name: ${userPreferences.data.username}")

        Row(
            Modifier.horizontalScroll(rememberScrollState())
        ) {
            names.forEach { Button({ viewModel.updateName(it) }) { Text(it) } }
        }

        Text("Selected Theme: ${userPreferences.data.theme}")

        Column {
            SelectedTheme.entries.forEach {
                Text(
                    text = it.string,
                    modifier = Modifier
                        .border(5.dp, MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                        .clickable {
                            viewModel.updateTheme(it)
                            onUpdateTheme(it)
                        },
                    style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

                )
            }
        }

        Button({
            viewModel.clearData()
        }) {
            Text("clear all data")
        }

        Text("loading: ${userPreferences.loading}, data ${userPreferences.data}")


    }

}