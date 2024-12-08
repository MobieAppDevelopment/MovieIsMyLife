package com.example.movieismylife

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movieismylife.viewmodel.MyPageViewModel
import com.example.movieismylife.viewmodel.SignInState
import com.example.movieismylife.viewmodel.SignInViewModel

@Composable
fun MyPage(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    signInViewModel: SignInViewModel
    ) {
    val uiState by signInViewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { MovieBottomBar(navController=navController) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            color = Color(0xFF1A1B1E)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Profile Section (Same as before)
                when (val state = uiState) {
                    is SignInState.Success -> {
                        val user = state.user
                        Log.d("test", user.name)
                        Log.d("test", user.id)
                        ProfileSection(user.name, user.id)
                    }

                    is SignInState.Error -> {}
                    is SignInState.Loading -> {}
                    is SignInState.Nothing -> {Log.d("now", "nothing")}
                }

                // Stats Section (Same as before)
                StatsSection()

                // Review Sections (Now with click handlers)
                ReviewSection(
                    title = "작성한 리뷰",
                    count = "0",
                    onClick = { navController.navigate("writtenReviews") }
                )
                ReviewSection(
                    title = "좋아요한 리뷰",
                    count = "0",
                    onClick = { navController.navigate("likedReviews") }
                )
            }
        }
    }
}

@Composable
fun ProfileSection(name: String, id: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B4155))
        ) {
            Text(
                text = name.substring(0 until 1),
                color = Color.White,
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(
            text = name,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "ID ${id}",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .background(Color(0xFF242632))
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("찜했어요", "0")
        StatItem("보는중", "0")
        StatItem("봤어요", "0")
    }
}

@Composable
fun StatItem(label: String, count: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ReviewSection(title: String, count: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp
        )
        Text(
            text = count,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

