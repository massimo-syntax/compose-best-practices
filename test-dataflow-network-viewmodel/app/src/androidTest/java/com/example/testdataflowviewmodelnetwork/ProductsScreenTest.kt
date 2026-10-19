package com.example.testdataflowviewmodelnetwork

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.testdataflowviewmodelnetwork.domain.model.Product
import com.example.testdataflowviewmodelnetwork.presentation.state.ProductUiState
import com.example.testdataflowviewmodelnetwork.presentation.ui.ProductsContent
import org.junit.Rule
import org.junit.Test

class ProductsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun show_loading_indicator_when_uiState_is_Loading() {
        composeTestRule.setContent {
            ProductsContent(uiState = ProductUiState.Loading)
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun show_products_list_when_uiState_is_Success() {
        val products = listOf(
            Product(id = "1", name = "Keyboard", price = 49.99f, inStock = true),
            Product(id = "2", name = "Mouse", price = 25.00f, inStock = false),
        )

        composeTestRule.setContent {
            ProductsContent(uiState = ProductUiState.Success(products))
        }

        composeTestRule.onNodeWithContentDescription("Products").assertIsDisplayed()
        composeTestRule.onNodeWithText("Keyboard").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mouse").assertIsDisplayed()
    }

    @Test
    fun show_error_message_when_uiState_is_Error() {
        val errorMessage = "Failed to load products"

        composeTestRule.setContent {
            ProductsContent(uiState = ProductUiState.Error(errorMessage))
        }

        composeTestRule.onNodeWithContentDescription("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun display_nothing_when_uiState_is_Idle() {
        composeTestRule.setContent {
            ProductsContent(uiState = ProductUiState.Idle)
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Products").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Error").assertDoesNotExist()
    }
}