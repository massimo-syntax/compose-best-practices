package com.example.testdataflowviewmodelnetwork.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.testdataflowviewmodelnetwork.domain.model.Product

@Composable
fun ProductItem(
    product: Product,
){
    val inStock = if(product.inStock) "in stock" else "out of stock"
    Column {
        Row {
            Text(product.name)
        }
        Row {
            Text("${product.price}")
            Text(inStock)
        }
    }
    HorizontalDivider()
}