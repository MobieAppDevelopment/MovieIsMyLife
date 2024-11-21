package com.example.movieismylife

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieismylife.ui.theme.MovieIsMyLifeTheme
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel
import com.example.movieismylife.viewmodel.MovieReviewViewModel


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            createNotificationChannel(this)
            showMovieRecommendationNotification(this)
        } else {
            Log.d("Notification", "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 앱 처음 사용시 알림 허용 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        } else {
            createNotificationChannel(this)
            showMovieRecommendationNotification(this)
        }

        enableEdgeToEdge()
        setContent {
            MovieIsMyLifeTheme {
                val movieListViewModel = viewModel<MovieListViewModel>() // 타입을 명시적으로 지정
                movieListViewModel.fetchPopularMovies()
                movieListViewModel.fetchGenres()

                //MainPage(movieListViewModel = movieListViewModel)

                val movieReview = viewModel<MovieReviewViewModel>()

//                MovieReviewManagementPage(viewModel = movieReview)

                // 임의로 일단 Search를 적용한 것
                // 나중에 search button에 navigation과 함께 적용해야 함
//                movieListViewModel.fetchSearchMovies("인생", page = 1)
//                SearchResultScreen(movieListViewModel = movieListViewModel)

                // movieDetail api test
                val movieDetailViewModel = viewModel<MovieDetailViewModel>() // 타입을 명시적으로 지정
                MovieDetailPage(
                    movieDetailViewModel = movieDetailViewModel,
                    movieReviewViewModel = movieReview
                    )
                movieDetailViewModel.fetchMovieDetail()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            createNotificationChannel(this)
            showMovieRecommendationNotification(this)
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