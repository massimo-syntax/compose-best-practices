package com.example.testdataflowviewmodelnetwork.domain.usecase

import com.example.testdataflowviewmodelnetwork.domain.model.Product
import com.example.testdataflowviewmodelnetwork.domain.repository.ProductRepository
import com.example.testdataflowviewmodelnetwork.domain.result.FetchResult

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): FetchResult<List<Product>> =
        repository.getProducts()
}
