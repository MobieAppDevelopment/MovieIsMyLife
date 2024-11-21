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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.movieismylife.R
import com.example.movieismylife.model.Movies
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    navController: NavController,
    movieListViewModel: MovieListViewModel,
    movieDetailViewModel: MovieDetailViewModel
) {
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        bottomBar = { MovieBottomBar(navController=navController) }
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
                        Top20MovieCard(
                            movie = movie,
                            rank = index,
                            clickDetailEvent = {
                                movieDetailViewModel.fetchMovieDetail(it.id)
                                navController.navigate("detail")
                            }
                        )
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
                        SmallMovieCard(
                            movie = movie,
                            clickDetailEvent = {
                                movieDetailViewModel.fetchMovieDetail(it.id)
                                navController.navigate("detail")
                            }
                        )
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
                        SmallMovieCard(
                            movie = movie,
                            clickDetailEvent = {
                                movieDetailViewModel.fetchMovieDetail(it.id)
                                navController.navigate("detail")
                            }
                        )
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
                        SmallMovieCard(
                            movie = movie,
                            clickDetailEvent = {
                                movieDetailViewModel.fetchMovieDetail(it.id)
                                navController.navigate("detail")
                            }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Top20MovieCard(
    movie: Movies,
    rank:Int,
    clickDetailEvent: (movie: Movies) -> Unit
    ) {
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(400.dp)
            .clickable {
                clickDetailEvent(movie)
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
fun SmallMovieCard(
    movie: Movies,
    clickDetailEvent: (movie: Movies) -> Unit
) {
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clickable {
                clickDetailEvent(movie)
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


@Composable
fun MovieBottomBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.DarkGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                navController.navigate("main")
            }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Main",
                    tint = Color.White
                )
            }
        }
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                navController.navigate("search")
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                navController.navigate("mypage")
            }) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "MyPage",
                    tint = Color.White
                )
            }
        }
    }
}