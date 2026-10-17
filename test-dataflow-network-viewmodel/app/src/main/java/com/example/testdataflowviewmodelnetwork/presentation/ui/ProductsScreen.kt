package com.example.testdataflowviewmodelnetwork.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testdataflowviewmodelnetwork.domain.model.Product
import com.example.testdataflowviewmodelnetwork.presentation.state.ProductUiState
import com.example.testdataflowviewmodelnetwork.presentation.ui.components.ProductsList
import com.example.testdataflowviewmodelnetwork.presentation.ui.components.ShowError
import com.example.testdataflowviewmodelnetwork.presentation.viewmodel.ProductViewModel

@Composable
fun ProductsScreen(
    viewModel: ProductViewModel = viewModel(),
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val modifier = Modifier.fillMaxSize().statusBarsPadding()

    when(uiState){
        ProductUiState.Idle -> Unit
        ProductUiState.Loading -> {
            CircularProgressIndicator()
        }
        is ProductUiState.Success -> {
            ProductsList(products = (uiState as ProductUiState.Success).data )
        }
        is ProductUiState.Error -> {
            ShowError(message = (uiState as ProductUiState.Error).message)
        }
    }
}














