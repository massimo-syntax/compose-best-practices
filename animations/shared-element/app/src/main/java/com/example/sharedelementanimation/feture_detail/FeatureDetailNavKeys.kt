package com.example.sharedelementanimation.feture_detail

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ItemDetailKey(
    val id: Int,
    val title: String,
    val image: Int // R.drawable.cpu
) : NavKey