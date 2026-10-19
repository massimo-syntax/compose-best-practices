package com.example.testdataflowviewmodelnetwork.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testdataflowviewmodelnetwork.presentation.state.ProductUiState
import com.example.testdataflowviewmodelnetwork.presentation.ui.components.ProductsList
import com.example.testdataflowviewmodelnetwork.presentation.ui.components.ShowError
import com.example.testdataflowviewmodelnetwork.presentation.viewmodel.ProductViewModel

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductsContent(
        uiState = uiState,
        modifier = modifier.fillMaxSize().statusBarsPadding()
    )
}

@Composable
fun ProductsContent(
    uiState: ProductUiState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        ProductUiState.Idle -> Unit
        ProductUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.testTag("loading_indicator")
            )
        }
        is ProductUiState.Success -> {
            ProductsList(
                modifier = modifier,
                products = uiState.data
            )
        }
        is ProductUiState.Error -> {
            ShowError(
                modifier = modifier,
                message = uiState.message
            )
        }
    }
}














