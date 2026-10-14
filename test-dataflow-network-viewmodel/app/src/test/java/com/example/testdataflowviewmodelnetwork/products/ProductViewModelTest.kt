package com.example.testdataflowviewmodelnetwork.products

import com.example.testdataflowviewmodelnetwork.MainDispatcherRule
import com.example.testdataflowviewmodelnetwork.domain.model.Product
import com.example.testdataflowviewmodelnetwork.domain.result.FetchError
import com.example.testdataflowviewmodelnetwork.domain.result.FetchResult
import com.example.testdataflowviewmodelnetwork.domain.usecase.GetProductsUseCase
import com.example.testdataflowviewmodelnetwork.presentation.state.ProductUiState
import com.example.testdataflowviewmodelnetwork.presentation.viewmodel.ProductViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeProductRepository
    private lateinit var useCase: GetProductsUseCase
    private lateinit var viewModel: ProductViewModel

    @Before
    fun setUp(){
        fakeRepository = FakeProductRepository()
        useCase = GetProductsUseCase(fakeRepository)
        viewModel = ProductViewModel(useCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
    @Test
    fun `emit Loading then Success when useCase succeeds`() = runTest{
        val products = listOf(
            Product("123", "Product-1", 50.99f,true)
        )
        fakeRepository.result = FetchResult.Success(products)
        viewModel.fetchProducts()
        // wait until coroutine has finished
        advanceUntilIdle()
        assertEquals(ProductUiState.Success(products), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `emit Loading then Error when useCase fails`() = runTest {
        fakeRepository.result = FetchResult.Failure(FetchError.NetworkError)
        viewModel.fetchProducts()
        advanceUntilIdle()
        assertEquals(ProductUiState.Error("Error occurred"), viewModel.uiState.value)
    }
}