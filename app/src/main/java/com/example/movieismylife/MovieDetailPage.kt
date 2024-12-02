package com.example.movieismylife

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieismylife.enum.SortOption
import com.example.movieismylife.response.MovieDetailResponse
import com.example.movieismylife.ui.MovieDetailReviews
import com.example.movieismylife.ui.ReviewItem
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel
import com.example.movieismylife.viewmodel.MovieReviewViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailPage(
    navController: NavController,
    movieDetailViewModel: MovieDetailViewModel,
    movieReviewViewModel: MovieReviewViewModel,
    reviewViewModel: ReviewViewModel
) {
    // Sample data
    val movieDetail = movieDetailViewModel._movieDetail.value
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    val year = movieDetail?.releaseDate?.substring(0, 4)
    var isMovieDetail by rememberSaveable{ mutableStateOf(true) }

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
            Column {
                Card(
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                    ),
                ) {
                    Box(
                        modifier = Modifier.height(340.dp)
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
                        Box(
                            modifier = Modifier
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
                            Card(
                                shape = RoundedCornerShape(8.dp)
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
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 130.dp, end = 12.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = movieDetail?.title ?: "제목없음",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(4.dp)
                                )
                                Text(
                                    text = year ?: "날짜 없음",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(10.dp)
                                )
                                Row {
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
                                        text = String.format(
                                            "%.1f",
                                            movieDetail?.voteAverage ?: 0.0
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.clickable(onClick = { if(!isMovieDetail) isMovieDetail = !isMovieDetail })
                    ) {
                        Text(
                            text = "작품 정보",
                            color = Color.White, // Set the text color
                            fontSize = 18.sp // Set the text size
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(120.dp)
                    )
                    Box(
                        modifier = Modifier.clickable(onClick = { if(isMovieDetail) isMovieDetail = !isMovieDetail })
                    ) {
                        Text(
                            text = "리뷰",
                            color = Color.White, // Set the text color
                            fontSize = 18.sp // Set the text size
                        )
                    }
                }
                if(isMovieDetail) MovieDetailInfo(movieDetail)
                else Review(
                    navController,
                    movieReviewViewModel,
                    reviewViewModel,
                    movieDetail?.id.toString()
                )
            }
        }
    }
}

@Composable
fun MovieDetailInfo(movieDetail : MovieDetailResponse?) {
    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
    ){
        Text(
            text = movieDetail?.overview ?: "제목없음",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp)
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Row(

        ){
            Column(

            ){
                Text(
                    text = "장르",
                    color = Color.LightGray
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text(
                    text = "개봉일",
                    color = Color.LightGray
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text(
                    text = "제작국가",
                    color = Color.LightGray
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text(
                    text = "시간",
                    color = Color.LightGray
                )
            }
            Spacer(
                modifier = Modifier
                    .width(46.dp)
            )
            Column(

            ){
                Row {
                    movieDetail?.genres?.forEach { genre ->
                        Text(
                            text = genre.name,
                            color = Color.White
                        )
                        Spacer(
                            modifier = Modifier
                                .width(6.dp)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text(
                    text = movieDetail?.releaseDate?: "없음",
                    color = Color.White
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text(
                    text = movieDetail?.productionCompanies?.get(0)?.name?: "없음",
                    color = Color.White
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text(
                    text = Integer.toString(movieDetail?.runtime?: 0),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun Review(
    navController: NavController,
    movieReviewViewModel: MovieReviewViewModel,
    reviewViewModel: ReviewViewModel,
    movieId: String
    ) {
    val comments by reviewViewModel.comments.collectAsState()

    movieReviewViewModel.setSortOption(SortOption.LATEST)
//    reviewViewModel.createReview("1", movieId,"Amazing Movie!", 5f)
    reviewViewModel.loadComments(movieId)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(comments) { comment ->
            MovieDetailReviews(comment, navController)
        }
    }
}
