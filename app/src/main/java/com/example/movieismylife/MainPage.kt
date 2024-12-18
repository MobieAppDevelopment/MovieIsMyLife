package com.example.movieismylife

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieismylife.model.Movies
import com.example.movieismylife.viewmodel.CreditViewModel
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel
import com.example.movieismylife.viewmodel.SignInState
import com.example.movieismylife.viewmodel.SignInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    navController: NavController,
    movieListViewModel: MovieListViewModel,
    movieDetailViewModel: MovieDetailViewModel,
    reviewViewModel: ReviewViewModel,
    signInViewModel: SignInViewModel,
    creditViewModel: CreditViewModel
) {
    // Sample data
    val popularMovies = movieListViewModel.popularMovieList.value
    val actionMovies = movieListViewModel.getGenreMovies(28)
    val comedyMovies = movieListViewModel.getGenreMovies(35)
    val familyMovies = movieListViewModel.getGenreMovies(10751)
    val uiState by signInViewModel.state.collectAsState()
    val getUserId = (uiState as? SignInState.Success)?.user?.id ?: -1
    val userId = getUserId.toString()

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
                                creditViewModel.fetchCast(it.id)
                                movieDetailViewModel.fetchVideo(it.id)
                                reviewViewModel.calculateAverageScore(movieId = it.id.toString())
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
                                creditViewModel.fetchCast(it.id)
                                movieDetailViewModel.fetchVideo(it.id)
                                reviewViewModel.calculateAverageScore(movieId = it.id.toString())
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
                                creditViewModel.fetchCast(it.id)
                                movieDetailViewModel.fetchVideo(it.id)
                                reviewViewModel.calculateAverageScore(movieId = it.id.toString())
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
                                creditViewModel.fetchCast(it.id)
                                movieDetailViewModel.fetchVideo(it.id)
                                reviewViewModel.calculateAverageScore(movieId = it.id.toString())
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
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                    popUpTo("search") { inclusive = true }
                    popUpTo("map") { inclusive = true }
                    popUpTo("my") { inclusive = true }
                }
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
                navController.navigate("search") {
                    popUpTo("main") { inclusive = true }
                    popUpTo("search") { inclusive = true }
                    popUpTo("map") { inclusive = true }
                    popUpTo("my") { inclusive = true }
                }
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
                navController.navigate("map") {
                    popUpTo("main") { inclusive = true }
                    popUpTo("search") { inclusive = true }
                    popUpTo("map") { inclusive = true }
                    popUpTo("my") { inclusive = true }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "map",
                    tint = Color.White
                )
            }
        }
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                navController.navigate("my") {
                    popUpTo("main") { inclusive = true }
                    popUpTo("search") { inclusive = true }
                    popUpTo("map") { inclusive = true }
                    popUpTo("my") { inclusive = true }
                }
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