package com.example.testingtddsimple.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FirstScreen(){
    Column(
        Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text("TDD")
        Text("Unit test is testing of small units of code, single functions")
        Text("What is not ui tests, or integration tests, which belong to AndroidTest folder, is in Test folder")
        Text("Unit testing are much faster than integration or ui testing, so is a good choice to have many of them rather than ui or integration tests, when feasible")
        Text("Arrange Act Assert: Arrange -> prepare all classes & or dependencies, Act -> take all results of the functions, Assert -> assert that the functions are working as expected")
        Text("Stub, are fake values created per hand, Mocking with Mockk has more functionality in it")
        Text("")
        Text("")
    }
}