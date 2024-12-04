package com.example.movieismylife.ui

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieismylife.ReplyPage
import com.example.movieismylife.model.Comment
import com.example.movieismylife.model.CommentView
import com.example.movieismylife.viewmodel.MovieReviewViewModel
import com.example.movieismylife.viewmodel.ReplyViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel

@Composable
fun MovieDetailReviews(
    review: CommentView,
    navController: NavController,
    reviewViewModel: ReviewViewModel,
    replyViewModel: ReplyViewModel
) {
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
                                .width(260.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 3.dp)
                        )
                        Text(
                            text = review.score.toString(),
                            color = Color.White
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
                            replyViewModel.loadReplies(movieId = review.movieId, commentId = review.commentId)
                            Log.w("MovieListViewModel", "영화 리스트가 비어있습니다.")
//                            reviewViewModel.updateData(review)
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