package com.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.domain.repository.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@Composable
fun NewModuleScreen(
    viewModel: NewModuleViewModel = hiltViewModel()
){
    Column(
        Modifier.padding(24.dp)
    ) {
        Text("hello form here")
    }
}


@HiltViewModel
class NewModuleViewModel @Inject constructor(
    val repo: ItemsRepository
) : ViewModel(){



}