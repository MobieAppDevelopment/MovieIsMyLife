package com.example.movieismylife.model

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.cos
import kotlin.math.sqrt

class LocationDataManager(private val context: Context) {
    //유저의 현재 위치
    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    //유저 기준 근처의 영화관
    private val _nearbyTheaters = MutableStateFlow<List<Place>>(emptyList())
    val nearbyTheaters: StateFlow<List<Place>> = _nearbyTheaters

    //지도 API 로딩중?
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val placesClient: PlacesClient = Places.createClient(context)
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val newLatLng = LatLng(location.latitude, location.longitude)
            updateLocation(newLatLng)
            Log.i("MyLocation", "위도: ${location.latitude}, 경도: ${location.longitude}")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            _isLoading.value = true
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000,
                100f,
                locationListener
            )
        }
    }

    fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    private fun updateLocation(location: LatLng) {
        _userLocation.value = location
        fetchNearbyTheaters(location)
    }

    private fun fetchNearbyTheaters(location: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val theaters = searchNearbyTheaters(location)
                _nearbyTheaters.value = theaters
            } catch (e: Exception) {
                Log.e("LocationDataManager", "근처 영화관 탐색 중 에러가 발생 했습니다.", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun searchNearbyTheaters(location: LatLng): List<Place> {
        val radius = 1000
        val bounds = getBoundsFromLatLng(location, radius)
        val token = AutocompleteSessionToken.newInstance()

        val queries = listOf("CGV", "롯데시네마", "메가박스")
        return queries.flatMap { query ->
            searchTheaters(bounds, token, query)
        }.distinctBy { it.id }
    }

    private suspend fun searchTheaters(bounds: RectangularBounds, token: AutocompleteSessionToken, query: String): List<Place> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(bounds)
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setQuery(query)
            .setSessionToken(token)
            .build()

        return try {
            val response = placesClient.findAutocompletePredictions(request).await()
            Log.d("MapViewModel", "찾은 장소 개수: ${response.autocompletePredictions.size} 쿼리로 준 단어: '$query'")
            response.autocompletePredictions
                .filter { prediction ->
                    prediction.placeTypes.any { it == Place.Type.MOVIE_THEATER }
                }
                .mapNotNull { prediction ->
                    getPlaceDetails(prediction)
                }
        } catch (e: Exception) {
            Log.e("MapViewModel", "다음 쿼리로 호출하던 도중 예기치 못한 에러가 발생했습니다.: '$query'", e)
            emptyList()
        }
    }

    private suspend fun getPlaceDetails(prediction: AutocompletePrediction): Place? {
        val placeFields = listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES)
        val request = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)

        return try {
            val response = placesClient.fetchPlace(request).await()
            response.place
        } catch (e: Exception) {
            Log.e("MapViewModel", "장소 세부사항을 패치하던중 에러가 발생했습니다.", e)
            null
        }
    }

    private fun getBoundsFromLatLng(center: LatLng, radiusInMeters: Int): RectangularBounds {
        val distanceFromCenterToCorner = radiusInMeters * sqrt(2.0)
        val latRadian = Math.toRadians(center.latitude)

        val degLatKm = 100.0
        val degLongKm = 100.0 * cos(latRadian)
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

}