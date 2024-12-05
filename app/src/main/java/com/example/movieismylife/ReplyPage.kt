package com.example.movieismylife

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movieismylife.model.CommentView
import com.example.movieismylife.ui.MovieDetailReplies
import com.example.movieismylife.ui.MovieDetailReviews
import com.example.movieismylife.ui.getColorFromString
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
    onClickBackArrow: () -> Unit,
    ) {
//    val replyViewModel = viewModel<ReplyViewModel>()
    val replies by replyViewModel.replies.collectAsState()
    val comment by reviewViewModel.sharedData.collectAsState()
    var content by remember {
        mutableStateOf("")
    }

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
            Column(

            ) {
                IconButton(onClick = onClickBackArrow) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                commentDetail(reviewViewModel = reviewViewModel, movieDetailViewModel = movieDetailViewModel)
                Row(

                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 24.dp, start = 6.dp)
                            .size(40.dp) // 크기 설정
                            .clip(CircleShape) // 동그라미 모양
                            .background(getColorFromString(comment!!.profile)) // 배경색 설정
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "답글 추가") },
                        textStyle = TextStyle(color = Color.White),
                    )
                }
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
}

@Composable
fun commentDetail(
    reviewViewModel: ReviewViewModel,
    movieDetailViewModel: MovieDetailViewModel,
) {
    val comment by reviewViewModel.sharedData.collectAsState()
    val movieDetail = movieDetailViewModel._movieDetail.value
    var userLike by remember{ mutableStateOf(reviewViewModel._userLike) }
    var likeCount by remember{ mutableStateOf(reviewViewModel._likeCount) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(start = 17.dp, bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .height(140.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp) // 크기 설정
                            .clip(CircleShape) // 동그라미 모양
                            .background(getColorFromString(comment!!.profile)) // 배경색 설정
                    )
                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )
                    Text(
                        text = comment!!.name,
                        color = Color.White
                    )
                    Spacer(
                        modifier = Modifier
                            .width(190.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    if (userLike) {
                                        userLike = false
                                        likeCount -= 1
                                        reviewViewModel.deleteLikeUser(userId = "2", commentId = comment!!.commentId)
                                        reviewViewModel.deleteUserLike(userId = "2", commentId = comment!!.commentId)
                                    } else {
                                        userLike = true
                                        likeCount += 1
                                        reviewViewModel.createLike(userId = "2", comment!!.commentId)
                                        reviewViewModel.createUserLike(userId = "2", commentId = comment!!.commentId, movieTitle = movieDetail!!.title, moviePoster = movieDetail.posterPath ?: "") // 왜 moviePoster에는 !!가 안되고 ?: ""를 써야 되나?
                                    }

                                })
                            .padding(top = 4.dp)
                    ) {
                        if (userLike) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .width(4.dp)
                    )
                    Text(
                        text = "${likeCount}",
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 3.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                    )
                    Text(
                        text = comment!!.score.toString(),
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Text(
                    text = comment!!.content,
                    color = Color.White
                )
            }
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
        }
    }
}