package com.example.testdataflowviewmodelnetwork.presentation.viewmodel

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdataflowviewmodelnetwork.R
import com.example.testdataflowviewmodelnetwork.domain.result.FetchError
import com.example.testdataflowviewmodelnetwork.domain.result.FetchResult
import com.example.testdataflowviewmodelnetwork.domain.usecase.GetProductsUseCase
import com.example.testdataflowviewmodelnetwork.presentation.state.ProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun fetchProducts(){
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading

            when(val result = getProductsUseCase()){
                is FetchResult.Success -> {
                    _uiState.value = ProductUiState.Success(result.data)
                }
                is FetchResult.Failure -> {
                    _uiState.value = ProductUiState.Error(result.error.toErrorMessage())
                }
            }
        }
    }

}

private fun FetchError.toErrorMessage() =
    when(this){
        FetchError.NetworkError -> "error"
        FetchError.ServerError -> "error"
        FetchError.UnknownError -> "error"
    }