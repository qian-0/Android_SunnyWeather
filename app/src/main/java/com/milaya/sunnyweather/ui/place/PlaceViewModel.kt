package com.milaya.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.milaya.sunnyweather.logic.Repository
import com.milaya.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {
//    查询城市，调用 searchPlaces()，获取的数据存放在 placeLiveData 中
    private val searchLiveData = MutableLiveData<String>()
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

//    缓存页面上城市数据
    val placeList = ArrayList<Place>()

//    fun savePlace(place: Place) = Repository.savePlace(place)
//
//    fun getSavedPlace() = Repository.getSavedPlace()
//
//    fun isPlaceSaved() = Repository.isPlaceSaved()
}