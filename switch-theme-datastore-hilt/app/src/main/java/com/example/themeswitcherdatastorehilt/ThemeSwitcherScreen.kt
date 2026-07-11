package com.example.themeswitcherdatastorehilt

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ThemeSwitcherScreen(
    themeManager: ThemeViewModel = hiltViewModel()
) {
    val themeMode by themeManager.themeMode.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Appearance",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        ThemeOption(
            title = "Light",
            description = "Always use light theme",
            selected = themeMode is ThemeMode.Light,
            onClick = { themeManager.updateThemeMode(ThemeMode.Light) }
        )

        ThemeOption(
            title = "Dark",
            description = "Always use dark theme",
            selected = themeMode is ThemeMode.Dark,
            onClick = { themeManager.updateThemeMode(ThemeMode.Dark) }
        )

        ThemeOption(
            title = "Follow system",
            description = "Match your device settings",
            selected = themeMode is ThemeMode.System,
            onClick = { themeManager.updateThemeMode(ThemeMode.System) }
        )

        ThemeOption(
            title = "High contrast",
            description = "Optimized for accessibility",
            selected = themeMode is ThemeMode.HighContrast,
            onClick = { themeManager.updateThemeMode(ThemeMode.HighContrast) }
        )
    }

}

@Composable
fun ThemeOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: ()->Unit
){
    Surface(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                2.dp,
                if(selected) MaterialTheme.colorScheme.onBackground
                       else Color.Transparent,
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, color = MaterialTheme.colorScheme.primary)
            Text(description, color = MaterialTheme.colorScheme.primary)
        }
    }
}