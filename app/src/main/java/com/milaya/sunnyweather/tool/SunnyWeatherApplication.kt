package com.milaya.sunnyweather.tool

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// FUNCTION：为项目提供一种全局获取 Context 的方式

class SunnyWeatherApplication : Application(){
    companion object {
        const val TOKEN = "bAYPN3iI8dCEduIr"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}