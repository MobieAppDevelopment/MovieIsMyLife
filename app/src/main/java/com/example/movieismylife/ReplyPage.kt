package com.example.movieismylife

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movieismylife.model.CommentView
import com.example.movieismylife.ui.MovieDetailReplies
import com.example.movieismylife.ui.MovieDetailReviews
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.ReplyViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.StateFlow


@Composable
fun ReplyPage(
    navController : NavController,
    reviewViewModel: ReviewViewModel,
    replyViewModel: ReplyViewModel,
    movieDetailViewModel: MovieDetailViewModel,
    commentId : String
) {
//    val replyViewModel = viewModel<ReplyViewModel>()
//    val comment by reviewViewModel.sharedData.collectAsState()
    val replies by replyViewModel.replies.collectAsState()

//    replyViewModel.createReply("1", comment?.movieId ?: "", comment?.commentId ?: "", "I think so too")
    // 데이터 로딩 함수 호출을 제어
//    replyViewModel.loadReplies(comment?.commentId ?: "")


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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(replies) { reply ->
                    MovieDetailReplies(reply)
                }
            }
        }
        }
}