package com.example.movieismylife.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movieismylife.model.LocationDataManager

class MapViewModel(private val locationDataManager: LocationDataManager) : ViewModel() {
    //유저의 현재 위치
    val userLocation = locationDataManager.userLocation
    //유저의 현재 위치 근처에 있는 영화관 리스트
    val nearbyTheaters = locationDataManager.nearbyTheaters
    // 지도 API를 호출 중인가?
    val isLoading = locationDataManager.isLoading

    //사용자 편의성을 위해 미리 지도 API를 호출
    init {
        locationDataManager.startLocationUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        locationDataManager.stopLocationUpdates()
    }
}