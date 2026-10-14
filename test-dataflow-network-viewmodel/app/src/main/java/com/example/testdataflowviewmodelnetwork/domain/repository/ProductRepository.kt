package com.example.testdataflowviewmodelnetwork.domain.repository

import com.example.testdataflowviewmodelnetwork.domain.model.Product
import com.example.testdataflowviewmodelnetwork.domain.result.FetchResult

interface ProductRepository {

    suspend fun getProducts(): FetchResult<List<Product>>

}