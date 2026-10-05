package com.example.testapi.presentation



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapi.domain.Product
import com.example.testapi.domain.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ProductsViewModel(
    private val repository: ProductRepository
): ViewModel() {

    private var hasLoadedProducts = false

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products
        .onStart {
            // don't request again by resubscription to this flow
            // ie rotating the device
            if(!hasLoadedProducts) {
                _products.value = repository.getProducts()
                hasLoadedProducts = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _products.value
        )

}