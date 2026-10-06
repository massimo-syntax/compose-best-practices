package com.example.testapi.data

import kotlinx.serialization.Serializable

@Serializable
data class ProductsDto(
    val products: List<ProductDto>
)
