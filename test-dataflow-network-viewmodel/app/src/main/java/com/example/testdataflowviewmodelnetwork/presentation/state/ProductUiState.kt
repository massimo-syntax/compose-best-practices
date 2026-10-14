package com.example.testdataflowviewmodelnetwork.presentation.state

import com.example.testdataflowviewmodelnetwork.domain.model.Product

interface ProductUiState {
    data object Idle : ProductUiState
    data object Loading : ProductUiState
    data class Success(val data: List<Product>) : ProductUiState
    data class Error(val message: String) : ProductUiState
}
