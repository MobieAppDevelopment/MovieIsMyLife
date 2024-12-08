package com.example.movieismylife

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieismylife.model.LocationDataManager
import com.example.movieismylife.ui.theme.MovieIsMyLifeTheme
import com.example.movieismylife.viewmodel.MapViewModel
import com.example.movieismylife.viewmodel.MovieDetailViewModel
import com.example.movieismylife.viewmodel.MovieListViewModel
import com.example.movieismylife.viewmodel.MovieReviewViewModel
import com.example.movieismylife.viewmodel.ReplyViewModel
import com.example.movieismylife.viewmodel.MyPageViewModel
import com.example.movieismylife.viewmodel.ReviewViewModel
import com.example.movieismylife.viewmodel.SignInViewModel
import com.example.movieismylife.viewmodel.SignUpViewModel
//import com.example.movieismylife.viewmodel.UserViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kotlin.math.sign


class MainActivity : ComponentActivity() {
    lateinit var placesClient: PlacesClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            createNotificationChannel(this)
        } else {
            Log.d("Notification", "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        } else {
            createNotificationChannel(this)
        }

        requestLocationPermission()
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)

        val locationDataManager = LocationDataManager(applicationContext)

        enableEdgeToEdge()
        setContent {
            MovieIsMyLifeTheme {
                val navController = rememberNavController()
                val movieListViewModel = viewModel<MovieListViewModel>()
                val movieDetailViewModel = viewModel<MovieDetailViewModel>()
                val movieReviewViewModel = viewModel<MovieReviewViewModel>()
                val reviewViewModel = viewModel<ReviewViewModel>()
                val replyViewModel = viewModel<ReplyViewModel>()
                val signUpViewModel = viewModel<SignUpViewModel>()
                val signInViewModel = viewModel<SignInViewModel>()

                val mapViewModel: MapViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            MapViewModel(locationDataManager)
                        }
                    }
                )

                // Fetch popular movies only once
                LaunchedEffect(Unit) {
                    movieListViewModel.fetchPopularMovies()
                    movieListViewModel.fetchGenres()
                }

                // Observe popular movies and show notification when ready
                LaunchedEffect(movieListViewModel.isPopularMoviesFetched) {
                    movieListViewModel.isPopularMoviesFetched.collect { isFetched ->
                        if (isFetched) {
                            val randomMovie = movieListViewModel.getRandomMovie()
                            if (randomMovie != null) {
                                showMovieRecommendationNotification(this@MainActivity, randomMovie)
                            }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "signin",
                ){
                    composable(route = "signin"){
                        SignInPage(
                            navController=navController,
                            signInViewModel=signInViewModel
//                            userViewModel=userViewModel
                        )
                    }
                    composable(route = "signup"){
                        SignUpPage(
                            navController = navController,
                            signUpViewModel = signUpViewModel,
                        )
                    }
                    composable(
                        route = "main",
                        enterTransition = { slideInHorizontally() },
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
                            movieReviewViewModel=movieReviewViewModel,
                            reviewViewModel=reviewViewModel,
                            replyViewModel=replyViewModel,
                            onClickBackArrow = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = "search",
                        enterTransition = { slideInHorizontally() },
                        exitTransition = { slideOutHorizontally() }
                    ){
                        SearchResultPage(
                            movieListViewModel = movieListViewModel,
                            movieDetailViewModel = movieDetailViewModel,
                            navController=navController
                        )
                    }
                    composable(route = "map",
                        enterTransition = { slideInHorizontally() },
                        exitTransition = { slideOutHorizontally() }
                    ){
                        MovieTheaterMapScreen(
                            navController=navController,
                            mapViewModel = mapViewModel,
                            onRequestLocationPermission = { requestLocationPermission() }
                        )
                    }
                    composable("replyPage/{movieId}&{commentId}", arguments = listOf(
                        navArgument("movieId") {
                            type = NavType.StringType
                        },
                        navArgument("commentId") {
                            type = NavType.StringType
                        }
                    )) {
                        val movieId = it.arguments?.getString("movieId") ?: ""
                        val commentId = it.arguments?.getString("commentId") ?: ""
                        ReplyPage(
                            navController = navController,
                            reviewViewModel = reviewViewModel,
                            replyViewModel = replyViewModel,
                            movieDetailViewModel = movieDetailViewModel,
                            onClickBackArrow = {
                                navController.popBackStack()
                                reviewViewModel.loadComments(movieId = movieId, userId = "2")
                            },
                            commentId = commentId
                        ) }
                    composable("theaterPage") { TheaterPage(
                        navController,
                        movieDetailViewModel
                    ) }
                    composable(route = "my",
                        enterTransition = { slideInHorizontally() },
                        exitTransition = { slideOutHorizontally() }
                    ) {
                        MyPage(
                            navController = navController,
                            myPageViewModel = MyPageViewModel(),
                            signInViewModel = signInViewModel,
                            reviewViewModel = reviewViewModel
                        )
                    }
                    composable(route = "writtenReviews/{userId}", arguments = listOf(
                        navArgument("userId") {
                            type = NavType.StringType
                        }),
                        enterTransition = { slideInHorizontally() },
                        exitTransition = { slideOutHorizontally() }
                    ) {
                        val userId = it.arguments?.getString("userId") ?: ""
                        MovieReviewManagementPage(
                            navController = navController,
                            userId = userId,
                            reviewViewModel = reviewViewModel,
                            onClickBackArrow = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(route = "likedReviews/{userId}", arguments = listOf(
                        navArgument("userId") {
                            type = NavType.StringType
                        }),
                        enterTransition = { slideInHorizontally() },
                        exitTransition = { slideOutHorizontally() }
                    ) {
                        val userId = it.arguments?.getString("userId") ?: ""
                        MovieLikeReviewManagementPage(
                            navController = navController,
                            userId = userId,
                            reviewViewModel = reviewViewModel,
                            onClickBackArrow = {
                                navController.popBackStack()
                            })
                    }

                    composable(route = "reviewWrite") {
                        ReviewWritePage(navController = navController)
                    }
//                    composable(route = "my",
//                        enterTransition = { slideInHorizontally() },
//                        exitTransition = { slideOutHorizontally() }
//                    ) {
//                        MyPage(navController = navController)
//                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            createNotificationChannel(this)
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}