package com.example.testapi.domain

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}