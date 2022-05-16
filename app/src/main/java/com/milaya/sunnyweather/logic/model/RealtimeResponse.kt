package com.milaya.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, @SerializedName("server_time") val serverTime: Long, val result: Result) {
    data class Result(val realtime: Realtime)

    data class Realtime(val temperature: Float, val humidity: Float, val skycon: String, val wind: Wind, @SerializedName("apparent_temperature") val ApparentTemperature: Float, @SerializedName("air_quality") val airQuality: AirQuality)

    data class Wind(val speed: Float, val direction: Int)

    data class AirQuality(val pm25: Int, val pm10: Int, val o3: Int, val so2: Int, val no2: Int, val co: Float, val aqi: AQI, val description: Description)

    data class AQI(val chn: Int)

    data class Description(val chn: String)
}

