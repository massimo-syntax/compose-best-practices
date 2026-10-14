package com.example.testdataflowviewmodelnetwork.products

import com.example.testdataflowviewmodelnetwork.domain.model.Product
import com.example.testdataflowviewmodelnetwork.domain.repository.ProductRepository
import com.example.testdataflowviewmodelnetwork.domain.result.FetchResult
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class FakeProductRepository : ProductRepository {

    var result: FetchResult<List<Product>> = FetchResult.Success(emptyList())

    override suspend fun getProducts(): FetchResult<List<Product>>{
        delay(1.seconds)
        return result
    }

}