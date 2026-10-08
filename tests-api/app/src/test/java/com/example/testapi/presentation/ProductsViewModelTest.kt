package com.example.testapi.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import com.example.testapi.data.HttpClientFactory
import com.example.testapi.data.ProductRepositoryImpl
import com.example.testapi.data.http_mock_utls.JsonProductsResponse
import com.example.testapi.domain.ProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    private lateinit var viewModel: ProductsViewModel
    private lateinit var repository: ProductRepository
    private lateinit var httpClient: HttpClient
    private val testDispatcher = UnconfinedTestDispatcher()

    private var content = JsonProductsResponse.products
    private var statusCode = HttpStatusCode.OK

//    or make a small other data class:
//    private var responseData = HttpResponseData(
//        content = ProductsResponses.valid,
//        statusCode = HttpStatusCode.OK
//    )

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        httpClient = HttpClientFactory.create(
            engine = MockEngine.create {
                addHandler { req ->
                    val relativeUrl = req.url.encodedPath
                    when(relativeUrl) {
                        "/products" -> respond(
                            content = content,
                            status = statusCode,
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

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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

    @Test
    fun `API error returns empty products list`() = runBlocking {
        content = "error"
        statusCode = HttpStatusCode.Forbidden

        viewModel.products.test {
            val initialProducts = awaitItem()
            assertThat(initialProducts).isEmpty()

            expectNoEvents()
        }
    }

}
