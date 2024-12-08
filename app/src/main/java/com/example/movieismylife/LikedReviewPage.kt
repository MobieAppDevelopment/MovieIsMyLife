package com.example.movieismylife

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movieismylife.enum.SortOption
import com.example.movieismylife.ui.MovieDetailReviews
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieReviewViewModel
import com.example.movieismylife.viewmodel.ReplyViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel
import com.example.movieismylife.viewmodel.SignInState
import com.example.movieismylife.viewmodel.SignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedReviewPage(
    navController: NavController,
    signInViewModel: SignInViewModel,
    reviewViewModel: ReviewViewModel,
    movieDetailViewModel: MovieDetailViewModel,
    movieReviewViewModel: MovieReviewViewModel,
    replyViewModel: ReplyViewModel
) {
    val uiState = signInViewModel.state.collectAsState()

    val comments by reviewViewModel.comments.collectAsState(initial = emptyList())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1A1B1E)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        "좋아요한 리뷰",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1B1E)
                )
            )
            if (uiState.value == SignInState.Loading) {
                // 데이터가 로드되는 동안 CircularProgressIndicator 표시
                CircularProgressIndicator()
            } else {
                // 데이터가 로드된 후 Comment 리스트를 표시
                movieReviewViewModel.setSortOption(SortOption.LATEST)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(comments) { comment ->
                        MovieDetailReviews(
                            comment,
                            navController,
                            reviewViewModel,
                            replyViewModel,
                            movieDetailViewModel
                        )
                    }
                }
            }
        }
    }
}