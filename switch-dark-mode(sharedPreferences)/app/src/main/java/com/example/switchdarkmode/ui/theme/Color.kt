package com.example.switchdarkmode.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


data class AppColors(
    val colorPrimary: Color,
    val backgroundColor: Color,
    val cardBackgroundColor: Color,
    val dividerColor: Color,
    val inputFieldBorderColor: Color,
    val primaryTextColor: Color,
    val sectionHeaderColor: Color,
)

val LightAppColors = AppColors(
    colorPrimary          = Color(0xFF276EF7),
    backgroundColor       = Color(0xFFFBFBFB),
    cardBackgroundColor   = Color(0xFFFFFFFF),
    dividerColor          = Color(0xFFE0E0E0),
    inputFieldBorderColor = Color(0xFFCCCCCC),
    primaryTextColor      = Color(0xFF212121),
    sectionHeaderColor    = Color(0xFF757575),
)

val DarkAppColors = AppColors(
    colorPrimary          = Color(0xFFBDBDBD),
    backgroundColor       = Color(0xFF1E1F25),
    cardBackgroundColor   = Color(0xFF2C3638),
    dividerColor          = Color(0xFF3A3A3A),
    inputFieldBorderColor = Color(0xFF555555),
    primaryTextColor      = Color(0xFFEEEEEE),
    sectionHeaderColor    = Color(0xFF9E9E9E),
)


// staticCompositionLocalOf is the right choice here over compositionLocalOf.
// With compositionLocalOf, Compose tracks each consumer individually and recomposes them on change.
// With staticCompositionLocalOf, it invalidates the entire subtree
val LocalCustomColors = staticCompositionLocalOf { LightAppColors }
val LocalIsDarkTheme  = staticCompositionLocalOf { false }
