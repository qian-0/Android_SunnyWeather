package com.milaya.sunnyweather.tool

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// FUNCTION：Retrofit 构建器，帮助获取 Service 接口的动态代理对象

object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

//    使用 GSON 作为 JSON 数据转换库
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    简化 Retrofit.create() 函数
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}