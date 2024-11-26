package com.example.movieismylife

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieismylife.R
import com.example.movieismylife.model.Movies
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultPage(
    navController: NavController,
    movieListViewModel: MovieListViewModel,
    movieDetailViewModel: MovieDetailViewModel
) {
    // Sample data
    val searchResultMovies = movieListViewModel.searchMovieList.value
    var searchtext by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Search Result (${searchResultMovies.size})",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(horizontal = 10.dp)
                ){
                    OutlinedTextField(
                        value = searchtext,
                        onValueChange = {searchtext = it},
                        placeholder = {
                            Text(
                                text="Search",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                ) }, // Placeholder 사용
                        modifier = Modifier.weight(8f).height(52.dp),
                        colors = OutlinedTextFieldDefaults.colors(Color.White),
                        shape = RoundedCornerShape(16.dp), // 둥근 모서리 적용
                        singleLine = true, // 한 줄 입력만 가능
                        textStyle = MaterialTheme.typography.bodySmall.copy( // 텍스트 스타일 설정
                            fontSize = 14.sp, // 텍스트 크기 조정
                            color = Color.White
                        ),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ElevatedButton(
                        onClick = {
                            movieListViewModel.fetchSearchMovies(
                                query=searchtext, page=1
                            )
                        },
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.DarkGray),
                        modifier = Modifier.weight(2f).height(52.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        },
        bottomBar = { MovieBottomBar(navController=navController) }
    ){ innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color.Black
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(searchResultMovies) { index, movie ->
                        SearchMovieCard(
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
fun SearchMovieCard(
    movie: Movies,
    clickDetailEvent: (movie: Movies) -> Unit
) {
    val poster_path = "https://media.themoviedb.org/t/p/w220_and_h330_face/"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                clickDetailEvent(movie)
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