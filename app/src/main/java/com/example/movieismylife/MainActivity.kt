package com.example.movieismylife

import LoginPage
import SignUpPage
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieismylife.model.LocationDataManager
import com.example.movieismylife.ui.theme.MovieIsMyLifeTheme
import com.example.movieismylife.viewmodel.MapViewModel
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel
import com.example.movieismylife.viewmodel.MovieReviewViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient


class MainActivity : ComponentActivity() {
    lateinit var placesClient: PlacesClient

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

        //앱 처음 사용시 GPS 허용 요청
        requestLocationPermission()
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)

        val locationDataManager = LocationDataManager(applicationContext)

        enableEdgeToEdge()
        setContent {
            MovieIsMyLifeTheme {
                val navController = rememberNavController()
                val movieListViewModel = viewModel<MovieListViewModel>() // 타입을 명시적으로 지정
                movieListViewModel.fetchPopularMovies()
                movieListViewModel.fetchGenres()
                val movieDetailViewModel = viewModel<MovieDetailViewModel>() // 타입을 명시적으로 지정
                val movieReviewViewModel = viewModel<MovieReviewViewModel>()

                val mapViewModel: MapViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            MapViewModel(locationDataManager)
                        }
                    }
                )

                NavHost(
                    navController = navController,
                    startDestination = "login",
                ){
                    composable(route = "login"){
                        LoginPage(navController=navController)
                    }
                    composable(route = "signup"){
                        SignUpPage(
                            backToLoginPage = {
                                navController.navigateUp()
                            }
                        )
                    }
                    composable(
                        route = "main",
                        enterTransition = { slideInHorizontally() }, // 슬라이드 인 효과
                        exitTransition = { slideOutHorizontally() }
                    ){
                        MainPage(
                            movieListViewModel = movieListViewModel,
                            movieDetailViewModel = movieDetailViewModel,
                            navController=navController,
                        )
                    }
                    composable(route = "detail"){
                        MovieDetailPage(
                            navController=navController,
                            movieDetailViewModel=movieDetailViewModel,
                            movieReviewViewModel=movieReviewViewModel
                        )
                    }
                    composable(
                        route = "search",
                        enterTransition = { slideInHorizontally() }, // 슬라이드 인 효과
                        exitTransition = { slideOutHorizontally() }
                    ){
                        SearchResultPage(
                            movieListViewModel = movieListViewModel,
                            movieDetailViewModel = movieDetailViewModel,
                            navController=navController
                        )
                    }
                    composable(route = "map",
                        enterTransition = { slideInHorizontally() }, // 슬라이드 인 효과
                        exitTransition = { slideOutHorizontally() }
                    ){
                        MovieTheaterMapScreen(navController=navController,
                            mapViewModel = mapViewModel,
                            onRequestLocationPermission = { requestLocationPermission() }
                            )
                    }
                }
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

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

}