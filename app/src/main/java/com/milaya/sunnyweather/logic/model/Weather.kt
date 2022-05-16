package com.milaya.sunnyweather.logic.model

data class Weather(val time: Long, val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)