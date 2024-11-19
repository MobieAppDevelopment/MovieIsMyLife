package com.example.movieismylife

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailPage(movieDetailViewModel: MovieDetailViewModel) {
    // Sample data
    val movieDetail = movieDetailViewModel._movieDetail.value
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    val year = movieDetail?.releaseDate?.substring(0, 4)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "MOVIE IS MY LIFE",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ){ innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Black
        ) {
            Card(
                modifier = Modifier
                    .padding(bottom = 420.dp)
                    .background(Color.White),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.LightGray,
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = poster_path + movieDetail?.backdropPath
                        ),
                        contentDescription = "Loaded image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop, // 이미지의 자르기와 맞춤 설정
                        alignment = Alignment.TopStart
                    )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black,
                                ),
                                startY = -200f,
                                endY = 900f
                            )
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, top = 110.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = poster_path + movieDetail?.posterPath
                            ),
                            contentDescription = "Loaded image",
                            modifier = Modifier
                                .width(150.dp)
                                .height(230.dp),
                            contentScale = ContentScale.Crop // 이미지의 자르기와 맞춤 설정
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 130.dp, end = 12.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = movieDetail?.title?: "제목없음",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(4.dp)
                            )
                            Text(
                                text = year?: "날짜 없음",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(10.dp)
                            )
                            Row{
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(top = 3.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                    .width(3.dp)
                                )
                                Text(
                                    text = String.format("%.1f", movieDetail?.voteAverage?: 0.0),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
