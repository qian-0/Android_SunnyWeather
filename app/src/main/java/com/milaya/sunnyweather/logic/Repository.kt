package com.milaya.sunnyweather.logic

import androidx.lifecycle.liveData
import com.milaya.sunnyweather.logic.dao.PlaceDao
import com.milaya.sunnyweather.logic.model.Place
import com.milaya.sunnyweather.logic.model.Weather
import com.milaya.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


// FUNCTION：仓库层，决定数据源

object Repository {
/*
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

    fun refreshWeather(lng: String, lat: String, dailysteps: Int) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
//                并发执行两个请求
                val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
                val deferredDaily = async { SunnyWeatherNetwork.getDailyWeather(lng, lat, dailysteps) }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()

                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(realtimeResponse.serverTime, realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}" + "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
        }catch (e: Exception) {
            Result.failure<Weather>(e)
        }
        emit(result)
    }
*/

//    liveData(), 自动构建并返回一个 LiveData() 对象
//    Dispatchers.IO, 使用子线程处理网络请求
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String, dailysteps: Int, placeName: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat, dailysteps)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.serverTime, realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" + "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

//    统一 try catch 处理
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}