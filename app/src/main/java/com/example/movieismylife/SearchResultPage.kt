package com.example.movieismylife

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
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
fun SearchResultScreen(movieListViewModel: MovieListViewModel) {
    // Sample data
    val searchResultMovies = movieListViewModel.searchMovieList.value
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
                .padding(innerPadding),
            color = Color.Black
        ) {
            Column {
                // Top 10 Section
                Text(
                    text = "Search Result (${searchResultMovies.size})",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(searchResultMovies) { index, movie ->
                        SearchMovieCard(movie = movie, rank = index)
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMovieCard(movie: Movies, rank:Int) {
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                println("clicked!!")
            },
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Movie Poster
        Card(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = poster_path+movie.posterPath
                ),
                contentDescription = "Loaded image",
                modifier = Modifier
                    .width(80.dp)
                    .height(160.dp),
                contentScale = ContentScale.Crop // 이미지의 자르기와 맞춤 설정
            )
        }

        // Movie Details
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RatingBar(rating = movie.voteAverage)
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}