package com.example.testdataflowviewmodelnetwork.products

import com.example.testdataflowviewmodelnetwork.domain.model.Product
import com.example.testdataflowviewmodelnetwork.domain.result.FetchError
import com.example.testdataflowviewmodelnetwork.domain.result.FetchResult
import com.example.testdataflowviewmodelnetwork.domain.usecase.GetProductsUseCase
import com.example.testdataflowviewmodelnetwork.presentation.viewmodel.ProductViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    private lateinit var useCase: GetProductsUseCase
    private lateinit var fakeRepository: FakeProductRepository

    @Before
    fun setUp(){
        fakeRepository = FakeProductRepository()
        useCase = GetProductsUseCase(fakeRepository)
    }


    @Test
    fun `Repository result and useCase return value are equals`() = runTest {
        val products = listOf(
            Product("123", "Product-1", 50.99f,true)
        )
        fakeRepository.result = FetchResult.Success(products)
        val result = useCase()
        assertEquals(fakeRepository.result , result)
    }

    @Test
    fun `when network error occurs useCase returns Network Error`() = runTest{
        fakeRepository.result = FetchResult.Failure(FetchError.NetworkError)
        val error = useCase()
        assertEquals(fakeRepository.result, error)
    }

}