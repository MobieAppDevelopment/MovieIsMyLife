package com.example.movieismylife.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movieismylife.model.LocationDataManager

class MapViewModel(private val locationDataManager: LocationDataManager) : ViewModel() {
    val userLocation = locationDataManager.userLocation
    val nearbyTheaters = locationDataManager.nearbyTheaters
    val isLoading = locationDataManager.isLoading

    init {
        locationDataManager.startLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        locationDataManager.stopLocationUpdates()
    }
}