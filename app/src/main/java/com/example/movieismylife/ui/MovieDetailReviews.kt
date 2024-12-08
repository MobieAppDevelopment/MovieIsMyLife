package com.example.movieismylife.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movieismylife.model.CommentView
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.ReplyViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel

@Composable
fun MovieDetailReviews(
    review: CommentView,
    navController: NavController,
    reviewViewModel: ReviewViewModel,
    replyViewModel: ReplyViewModel,
    movieDetailViewModel: MovieDetailViewModel
) {
    val movieDetail = movieDetailViewModel._movieDetail.value
    var userLike by remember { mutableStateOf(review.userLike) }
    var likeCount by remember { mutableStateOf(review.likeCount) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
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
                                .background(getColorFromString(review.profile)) // 배경색 설정
                        )
                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                        )
                        Text(
                            text = review.name,
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
                                    reviewViewModel.deleteLikeUser(userId = "2", commentId = review.commentId)
                                    reviewViewModel.deleteUserLike(userId = "2", commentId = review.commentId)
                                } else {
                                    userLike = true
                                    likeCount += 1
                                    reviewViewModel.createLike(userId = "2", review.commentId)
                                    reviewViewModel.createUserLike(userId = "2", commentId = review.commentId, movieTitle = movieDetail!!.title, moviePoster = movieDetail.posterPath ?: "") // 왜 moviePoster에는 !!가 안되고 ?: ""를 써야 되나?
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
                            text = review.score.toString(),
                            color = Color.White,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Text(
                        text = review.content,
                        color = Color.White
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp),
                    contentAlignment = Alignment.CenterEnd // 오른쪽 끝 정렬
                ) {
                    OutlinedButton(
                        onClick = {
                            navController.navigate("replyPage/${review.movieId}&${review.commentId}") // reply create할 때 ReplyPage에서 사용
                            replyViewModel.loadReplies(commentId = review.commentId)
                            reviewViewModel.updateData(review, userLike, likeCount)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(text = "답글")
                    }
                }
            }
        }
    }
}

fun getColorFromString(colorName: String): Color {
    return when (colorName.uppercase()) { // 대소문자 구분 없이 처리
        "WHITE" -> Color.White
        "BLACK" -> Color.Black
        "RED" -> Color.Red
        "GREEN" -> Color.Green
        "BLUE" -> Color.Blue
        else -> Color.Transparent // 기본값
    }
}