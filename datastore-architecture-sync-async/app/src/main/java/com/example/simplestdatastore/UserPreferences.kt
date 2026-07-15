package com.example.simplestdatastore

data class UserPreferences(
    val theme: SelectedTheme?,
    val username: String,
)

object StringToSelectedTheme{
    fun getThemeEnum(themeString:String?): SelectedTheme? =
        SelectedTheme.entries.firstOrNull {it.string == themeString}
}

