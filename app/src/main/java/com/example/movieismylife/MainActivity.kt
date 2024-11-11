package com.example.movieismylife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieismylife.model.MovieReview
import com.example.movieismylife.model.Movies
import com.example.movieismylife.ui.theme.MovieIsMyLifeTheme
import com.example.movieismylife.viewmodel.MovieListViewModel
import com.example.movieismylife.viewmodel.MovieReviewViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieIsMyLifeTheme {
                val movieListViewModel = viewModel<MovieListViewModel>() // 타입을 명시적으로 지정
                movieListViewModel.fetchPopularMovies()
                movieListViewModel.fetchGenres()

                //MainPage(movieListViewModel = movieListViewModel)

                val movieReview = viewModel<MovieReviewViewModel>()

                MovieReviewScreen(viewModel = movieReview)

                // 임의로 일단 Search를 적용한 것
                // 나중에 search button에 navigation과 함께 적용해야 함
//                movieListViewModel.fetchSearchMovies("인생", page = 1)
//                SearchResultScreen(movieListViewModel = movieListViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieIsMyLifeTheme {
        Greeting("Android")
    }
}