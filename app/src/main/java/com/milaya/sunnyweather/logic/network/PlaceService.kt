package com.milaya.sunnyweather.logic.network

import com.milaya.sunnyweather.logic.model.PlaceResponse
import com.milaya.sunnyweather.tool.SunnyWeatherApplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}