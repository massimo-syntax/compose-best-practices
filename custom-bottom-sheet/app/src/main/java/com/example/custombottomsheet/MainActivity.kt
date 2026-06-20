package com.example.custombottomsheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.example.custombottomsheet.ui.theme.CustomBottomSheetTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomBottomSheetTheme {
                //BasicBottomSheetScreen()
                //BasicCustomBottomSheetScreen()
                //BottomSheetWithPeekHeightScreen()
                //BasicCustomizedBottomSheetScreen()
                SlideCustomColumnScreen()
            }
        }
    }
}





