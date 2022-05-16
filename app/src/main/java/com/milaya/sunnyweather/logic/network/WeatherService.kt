package com.milaya.sunnyweather.logic.network

import com.milaya.sunnyweather.logic.model.DailyResponse
import com.milaya.sunnyweather.logic.model.RealtimeResponse
import com.milaya.sunnyweather.tool.SunnyWeatherApplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String, @Query("dailysteps") dailysteps: Int): Call<DailyResponse>
}