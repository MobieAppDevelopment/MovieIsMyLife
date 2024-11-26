package com.example.movieismylife

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailPage(
    navController: NavController,
    title: String = "작성한 리뷰",
    reviewCount: Int = 1
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1A1B1E)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${reviewCount}개의 리뷰",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
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
                actions = {
                    IconButton(onClick = { /* Sort functionality */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Sort",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF121314)) // 원하는 배경색 설정
            )

            // Review Item
            ReviewItem(
                movieTitle = "열혈사제 2",
                rating = 3.0f,
                reviewText = "나를 괜찮았다.",
                timestamp = "넷 초 전",
                onReviewClick = { /* Navigate to edit review */ },
                onLikeClick = { /* Handle like */ }
            )
            ReviewItem(
                movieTitle = "열혈사제 1",
                rating = 3.0f,
                reviewText = "똥과 같은 영화... 아아아앙아아아ㅏ아아아아ㅏㅇ아아아아아아아아아아아아아아",
                timestamp = "넷 초 전",
                onReviewClick = { /* Navigate to edit review */ },
                onLikeClick = { /* Handle like */ }
            )
        }
    }
}

@Composable
fun ReviewItem(
    movieTitle: String,
    rating: Float,
    reviewText: String,
    timestamp: String,
    onReviewClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onReviewClick), // Card에 클릭 효과 추가
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ), // 카드의 그림자 깊이 조절
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2B2E)
        ) // 카드 배경색 설정
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Movie Thumbnail
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = movieTitle,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column {
                        Text(
                            text = movieTitle,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "내 평가",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color.Yellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = rating.toString(),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Text(
                    text = timestamp,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Review Text
            Text(
                text = reviewText,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color.Gray
                    )
                }
                Text(
                    text = "좋아요",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = { /* Share functionality */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.Gray
                    )
                }
                Text(
                    text = "공유하기",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}


//@Composable
//fun ReviewItem(
//    movieTitle: String,
//    rating: Float,
//    reviewText: String,
//    timestamp: String,
//    onReviewClick: () -> Unit,
//    onLikeClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onReviewClick)
//            .padding(16.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                // Movie Thumbnail
//                Image(
//                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
//                    contentDescription = movieTitle,
//                    modifier = Modifier
//                        .size(48.dp)
//                        .padding(end = 8.dp),
//                    contentScale = ContentScale.Crop
//                )
//
//                Column {
//                    Text(
//                        text = movieTitle,
//                        color = Color.White,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text(
//                            text = "내 평가",
//                            color = Color.Gray,
//                            fontSize = 14.sp
//                        )
//                        Icon(
//                            imageVector = Icons.Default.Star,
//                            contentDescription = "Rating",
//                            tint = Color.Yellow,
//                            modifier = Modifier.size(16.dp)
//                        )
//                        Text(
//                            text = rating.toString(),
//                            color = Color.White,
//                            fontSize = 14.sp
//                        )
//                    }
//                }
//            }
//
//            Text(
//                text = timestamp,
//                color = Color.Gray,
//                fontSize = 12.sp
//            )
//        }
//
//        // Review Text
//        Text(
//            text = reviewText,
//            color = Color.White,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        // Action Buttons
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = onLikeClick) {
//                Icon(
//                    imageVector = Icons.Default.FavoriteBorder,
//                    contentDescription = "Like",
//                    tint = Color.Gray
//                )
//            }
//            Text(
//                text = "좋아요",
//                color = Color.Gray,
//                fontSize = 14.sp
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            IconButton(onClick = { /* Share functionality */ }) {
//                Icon(
//                    imageVector = Icons.Default.Share,
//                    contentDescription = "Share",
//                    tint = Color.Gray
//                )
//            }
//            Text(
//                text = "공유하기",
//                color = Color.Gray,
//                fontSize = 14.sp
//            )
//        }
//    }
//}