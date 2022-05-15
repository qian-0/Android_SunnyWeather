package com.milaya.sunnyweather.logic

import androidx.lifecycle.liveData
import com.milaya.sunnyweather.logic.model.Place
import com.milaya.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception


// FUNCTION：仓库层，决定数据源

object Repository {
//    liveData(), 自动构建并返回一个 LiveData() 对象
//    Dispatchers.IO, 使用子线程处理网络请求
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

//    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
//        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
//        if (placeResponse.status == "ok") {
//            val places = placeResponse.places
//            Result.success(places)
//        } else {
//            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
//        }
//    }

//    fun refreshWeather(lng: String, lat: String, placeName: String) = fire(Dispatchers.IO) {
//        coroutineScope {
//            val deferredRealtime = async {
//                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
//            }
//            val deferredDaily = async {
//                SunnyWeatherNetwork.getDailyWeather(lng, lat)
//            }
//            val realtimeResponse = deferredRealtime.await()
//            val dailyResponse = deferredDaily.await()
//            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
//                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
//                Result.success(weather)
//            } else {
//                Result.failure(
//                    RuntimeException(
//                        "realtime response status is ${realtimeResponse.status}" +
//                                "daily response status is ${dailyResponse.status}"
//                    )
//                )
//            }
//        }
//    }

//    fun savePlace(place: Place) = PlaceDao.savePlace(place)

//    fun getSavedPlace() = PlaceDao.getSavedPlace()

//    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

//    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
//        liveData<Result<T>>(context) {
//            val result = try {
//                block()
//            } catch (e: Exception) {
//                Result.failure<T>(e)
//            }
//            emit(result)
//        }

}