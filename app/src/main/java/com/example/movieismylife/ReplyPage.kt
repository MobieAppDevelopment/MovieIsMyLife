package com.example.movieismylife

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController


@Composable
fun ReplyPage(navController : NavController) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { MovieBottomBar(navController=navController) }
    ){ innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Black
        ) {

        }
        }
}