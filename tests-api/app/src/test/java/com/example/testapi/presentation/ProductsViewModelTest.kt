package com.example.testapi.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import com.example.testapi.data.HttpClientFactory
import com.example.testapi.data.ProductRepositoryImpl
import com.example.testapi.data.http_mock_utls.JsonProductsResponse
import com.example.testapi.domain.ProductRepository
import com.example.testapi.util.MainDispatcherRule
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductsViewModelTest {

    private lateinit var viewModel: ProductsViewModel
    private lateinit var repository: ProductRepository
    private lateinit var httpClient: HttpClient
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        httpClient = HttpClientFactory.create(
            engine = MockEngine.create {
                addHandler { req ->
                    val relativeUrl = req.url.encodedPath
                    when(relativeUrl) {
                        "/products" -> respond(
                            content = JsonProductsResponse.products,
                            status = HttpStatusCode.OK,
                            headers = headers {
                                //HttpHeaders.ContentType; ContentType.Application.Json
                                set("Content-Type", "application/json")
                            }
                        )
                        else -> respond(
                            content = "other paths are not supported",
                            status = HttpStatusCode.NotFound
                        )
                    }
                }
            }
        )

        repository = ProductRepositoryImpl(httpClient)
        viewModel = ProductsViewModel(repository)
    }

    @Test
    fun `Successful API call loads the products`() = runBlocking {
        // test state flow using turbine
        viewModel.products.test{
            val initialEmission = awaitItem()
            // initialValue = emptyList()
            assertThat(initialEmission).isEmpty()

            val products = awaitItem()
            assertThat(products).hasSize(30)
        }
    }


}
