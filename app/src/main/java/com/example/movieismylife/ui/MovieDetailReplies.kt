package com.example.movieismylife.ui

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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieismylife.ReplyPage
import com.example.movieismylife.model.Comment
import com.example.movieismylife.model.CommentView
import com.example.movieismylife.model.ReplyView
import com.example.movieismylife.viewmodel.MovieReviewViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel

@Composable
fun MovieDetailReplies(
    replyView: ReplyView
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.Black,
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
                                .background(getColorFromString(replyView.profile)) // 배경색 설정
                        )
                        Spacer(
                            modifier = Modifier
                                .width(10.dp)
                        )
                        Text(
                            text = replyView.name,
                            color = Color.White
                        )
                        Spacer(
                            modifier = Modifier
                                .width(260.dp)
                        )
                    }
                    Text(
                        text = replyView.content,
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
}