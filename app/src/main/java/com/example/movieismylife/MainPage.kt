package com.example.movieismylife

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.movieismylife.R
import com.example.movieismylife.model.Movies
import com.example.movieismylife.viewmodel.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(movieListViewModel: MovieListViewModel) {
    // Sample data
    val popularMovies = movieListViewModel.popularMovieList.value
    val actionMovies = movieListViewModel.getGenreMovies(28)
    val comedyMovies = movieListViewModel.getGenreMovies(35)
    val familyMovies = movieListViewModel.getGenreMovies(10751)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()), // 스크롤 가능하도록 설정
            color = Color.Black
        ) {
            Column {
                // Top 20 Section
                Text(
                    "TOP 20",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    itemsIndexed(popularMovies) { index, movie ->
                        Top20MovieCard(movie = movie, rank = index)
                    }
                }

                // Action Section
                Text(
                    "Action",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(actionMovies) { movie ->
                        SmallMovieCard(movie)
                    }
                }

                // comedy Section
                Text(
                    "Comedy",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(comedyMovies) { movie ->
                        SmallMovieCard(movie)
                    }
                }

                // family Section
                Text(
                    "Family",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(familyMovies) { movie ->
                        SmallMovieCard(movie)
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Top20MovieCard(movie: Movies, rank:Int) {
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(400.dp)
            .clickable {
                println("clicked!!")
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // 배경색 투명하게 설정
        ),
    ) {
        Column {
            Box{
                Image(
                    painter = rememberAsyncImagePainter(
                        model = poster_path+movie.posterPath
                    ),
                    contentDescription = "Loaded image",
                    modifier = Modifier
                        .width(200.dp)
                        .height(300.dp),
                    contentScale = ContentScale.Crop // 이미지의 자르기와 맞춤 설정
                )
                Text(
                    text = (rank+1).toString(),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart) // 상단 좌측 정렬
                        .background(Color.Black)
                        .padding(8.dp) // 카드 모서리로부터 여백
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RatingBar(rating = movie.voteAverage)
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SmallMovieCard(movie: Movies) {
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clickable {
                println("clicked!!")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = poster_path + movie.posterPath
            ),
            contentDescription = "Loaded image",
            modifier = Modifier
                .width(120.dp)
                .height(180.dp),
            contentScale = ContentScale.Crop // 이미지의 자르기와 맞춤 설정
        )
    }
}

@Composable
fun RatingBar(rating: Double) {
    Row {
        repeat(5) { index ->
            val starIcon = if (index < rating / 2) {
                Icons.Default.Favorite
            } else {
                Icons.Default.FavoriteBorder
            }
            Icon(
                imageVector = starIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}