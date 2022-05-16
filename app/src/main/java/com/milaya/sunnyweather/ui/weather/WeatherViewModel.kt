package com.milaya.sunnyweather.ui.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.milaya.sunnyweather.logic.Repository
import com.milaya.sunnyweather.logic.model.Location

class WeatherViewModel : ViewModel() {
    data class QueryPlace(val placeName: String, val dailysteps: Int, val location: Location)

    private val queryPlaceLiveData = MutableLiveData<QueryPlace>()
    fun refreshWeather(placeName: String, dailysteps: Int, lng: String, lat: String) {

//        Log.d("Test", "placeName: ${placeName}, dailysteps: ${ dailysteps}, lng: ${lng}, lat: ${lat}")

        queryPlaceLiveData.value = QueryPlace(placeName, dailysteps, Location(lng, lat))
    }
    val weatherLiveData = Transformations.switchMap(queryPlaceLiveData) { queryPlace ->
        Repository.refreshWeather(queryPlace.location.lng, queryPlace.location.lat, queryPlace.dailysteps,queryPlace.placeName)
    }

/*
    private val locationLiveData = MutableLiveData<Location>()
    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat, dailysteps, placeName)
    }
*/

    var placeName = ""
    var dailysteps = 0
    var locationLng = ""
    var locationLat = ""
}