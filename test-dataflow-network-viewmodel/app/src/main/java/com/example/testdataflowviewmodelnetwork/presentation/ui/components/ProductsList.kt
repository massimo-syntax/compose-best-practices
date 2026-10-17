package com.example.testdataflowviewmodelnetwork.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.testdataflowviewmodelnetwork.domain.model.Product

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    products: List<Product>
){
    Column(
        modifier.semantics{
            contentDescription = "Products"
        }
    ) {
        Text("Products")
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(products, key = {it.id}){
                ProductItem(product = it)
            }
        }
    }
}