package com.example.movieismylife

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.cos


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieTheaterMapScreen(
    navController: NavController,
    onRequestLocationPermission: () -> Unit
) {
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var nearbyTheaters by remember { mutableStateOf<List<Place>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val placesClient = remember {
        Places.createClient(context)
    }

    val locationManager = remember { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    val coroutineScope = rememberCoroutineScope()

    val locationListener = remember {
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val newLatLng = LatLng(location.latitude, location.longitude)
                userLocation = newLatLng
                Log.i("MyLocation", "위도: ${location.latitude}, 경도: ${location.longitude}")

                coroutineScope.launch {
                    isLoading = true
                    try {
                        val theaters = searchNearbyTheaters(context, placesClient, newLatLng)
                        nearbyTheaters = theaters
                    } catch (e: Exception) {
                        Log.e("MovieTheaterMapScreen", "theater 검색 중 에러 발생", e)
                    } finally {
                        isLoading = false
                    }
                }
            }

        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, // GPS 기반으로 나의 위치 파악
                    10000, // 재호출  최소 시간
                    100f, // 재호출 최소 거리
                    locationListener
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            locationManager.removeUpdates(locationListener)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TheaterMap",
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
        bottomBar = { MovieBottomBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (hasLocationPermission) {
                if (userLocation != null) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(userLocation!!, 13.5f) // 지도 시작 화면 크기 조정 가능
                        }
                    ) {
                        Marker( // 내 위치 마커 표시
                            state = MarkerState(position = userLocation!!),
                            title = "My Location",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                        )
                        nearbyTheaters.forEach { theater ->
                            theater.latLng?.let { latLng ->
                                Marker( // 영화관 위치 마커 표시
                                    state = MarkerState(position = latLng),
                                    title = theater.name ?: "Unknown Theater",
                                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                )
                            }
                        }
                    }
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else { // API 호출 하는 동안 화면
                    Text(
                        "Fetching location...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                Button(
                    onClick = onRequestLocationPermission,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text("Grant Location Permission")
                }
            }
        }
    }
}

suspend fun searchNearbyTheaters(context: android.content.Context, placesClient: PlacesClient, location: LatLng): List<Place> {
    return withContext(Dispatchers.IO) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("searchNearbyTheaters", "Coarse location permission not granted")
            return@withContext emptyList()
        }

        val radius = 1000
        val bounds = getBoundsFromLatLng(location, radius)
        val token = AutocompleteSessionToken.newInstance()

        val queries = listOf("CGV", "롯데시네마", "메가박스")
        //val queries = listOf("CGV", "롯데시네마", "메가박스", "극장", "영화관")
        val allTheaters = queries.map { query ->
            async {
                searchTheaters(placesClient, bounds, token, query)
            }
        }.awaitAll().flatten()

        Log.d("MovieTheaterMapScreen", "Total filtered theaters: ${allTheaters.size}")
        allTheaters.forEach { theater ->
            Log.d("MovieTheaterMapScreen", "Theater: ${theater.name}, LatLng: ${theater.latLng}")
        }

        allTheaters.distinctBy { it.id }
    }
}

private suspend fun searchTheaters(placesClient: PlacesClient, bounds: RectangularBounds, token: AutocompleteSessionToken, query: String): List<Place> {
    val request = FindAutocompletePredictionsRequest.builder()
        .setLocationBias(bounds)
        .setTypeFilter(TypeFilter.ESTABLISHMENT)
        .setQuery(query)
        .setSessionToken(token)
        .build()

    return try {
        val response = placesClient.findAutocompletePredictions(request).await()
        Log.d("MovieTheaterMapScreen", "Autocomplete predictions for '$query': ${response.autocompletePredictions.size}")

        response.autocompletePredictions
            .filter { prediction ->
                prediction.placeTypes.any { type ->
                    type == Place.Type.MOVIE_THEATER
                }
            }
            .mapNotNull { prediction ->
                getPlaceDetails(placesClient, prediction)
            }
    } catch (e: Exception) {
        Log.e("searchNearbyTheaters", "Error searching for nearby theaters with query '$query'", e)
        emptyList()
    }
}

private suspend fun getPlaceDetails(placesClient: PlacesClient, prediction: AutocompletePrediction): Place? {
    val placeFields = listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES)
    val request = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)

    return try {
        val response = placesClient.fetchPlace(request).await()
        response.place
    } catch (e: Exception) {
        Log.e("getPlaceDetails", "Error fetching place details", e)
        null
    }
}

private fun getBoundsFromLatLng(center: LatLng, radiusInMeters: Int): RectangularBounds {
    val distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0)
    val latRadian = Math.toRadians(center.latitude)

    val degLatKm = 110
    val degLongKm = 110 * cos(latRadian)
    val deltaLat = distanceFromCenterToCorner / 1000.0 / degLatKm
    val deltaLong = distanceFromCenterToCorner / 1000.0 / degLongKm

    val minLat = center.latitude - deltaLat
    val minLong = center.longitude - deltaLong
    val maxLat = center.latitude + deltaLat
    val maxLong = center.longitude + deltaLong

    return RectangularBounds.newInstance(
        LatLng(minLat, minLong),
        LatLng(maxLat, maxLong)
    )
}