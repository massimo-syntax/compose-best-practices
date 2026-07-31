package com.example.deeplinksparsing

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavKeySerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
data object HomeKey : NavKey

@Serializable
data class UserKey(val id: String) : NavKey

@Serializable
data class ProfileKey(
    val name: String?,
    val picture: String?,
    val favoriteFood: String?,
) : NavKey
