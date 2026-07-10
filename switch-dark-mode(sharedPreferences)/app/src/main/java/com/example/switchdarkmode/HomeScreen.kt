package com.example.switchdarkmode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.switchdarkmode.ui.theme.AppThemeColor


@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel) {
    val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()

    ThemeSettingsSection(
        currentTheme = themeMode,
        onThemeSelected = { themeViewModel.setTheme(it) }
    )
}



@Composable
fun ThemeSettingsSection(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    val options = listOf(
        ThemeMode.LIGHT  to "Light",
        ThemeMode.DARK   to "Dark",
        ThemeMode.SYSTEM to "System default"
    )

    Column(
        Modifier.background(AppThemeColor.colors.backgroundColor).padding(16.dp)
    ) {
        Text(
            text = "App theme",
            style = MaterialTheme.typography.labelLarge,
            color = AppThemeColor.colors.sectionHeaderColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        options.forEach { (mode, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeSelected(mode) }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentTheme == mode,
                    onClick = { onThemeSelected(mode) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppThemeColor.colors.primaryTextColor,
                        unselectedColor = AppThemeColor.colors.inputFieldBorderColor
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppThemeColor.colors.primaryTextColor
                )
            }
        }
    }
}
