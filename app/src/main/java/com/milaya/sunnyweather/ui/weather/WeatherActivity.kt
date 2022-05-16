package com.milaya.sunnyweather.ui.weather

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.milaya.sunnyweather.R
import com.milaya.sunnyweather.databinding.*
import com.milaya.sunnyweather.logic.model.Weather
import com.milaya.sunnyweather.tool.getSky
import java.text.SimpleDateFormat
import java.util.*

private lateinit var activityWeatherBinding: ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {
//    获得 WeatherViewModel 实例
    val viewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        融合状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }

        activityWeatherBinding = ActivityWeatherBinding.inflate(layoutInflater)
        val view  = activityWeatherBinding.root
        setContentView(view)

        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        if (viewModel.dailysteps == 0) {
            viewModel.dailysteps = intent.getIntExtra("dailysteps", 0)
        }
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        viewModel.refreshWeather(viewModel.placeName, viewModel.dailysteps, viewModel.locationLng, viewModel.locationLat)

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
//            swipeRefresh.isRefreshing = false
        })

//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
//        refreshWeather()
//        swipeRefresh.setOnRefreshListener {
//            refreshWeather()
//        }
//        navBtn.setOnClickListener {
//            drawerLayout.openDrawer(GravityCompat.START)
//        }
//        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
//            override fun onDrawerStateChanged(newState: Int) {}
//
//            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
//
//            override fun onDrawerOpened(drawerView: View) {}
//
//            override fun onDrawerClosed(drawerView: View) {
//                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//            }
//        })
    }

//    fun refreshWeather() {
//        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
//        swipeRefresh.isRefreshing = true
//    }

    private fun showWeatherInfo(weather: Weather) {
        val time = weather.time
        val realtime = weather.realtime
        val daily = weather.daily

        Log.d("Test", "time: ${time}, realtime: ${realtime}, daily: ${daily}")

        // 填充 now.xml 布局
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        val sky_1 = getSky(realtime.skycon)

        activityWeatherBinding.nowInclude.placeName.text = viewModel.placeName
        activityWeatherBinding.nowInclude.currentTemp.text = currentTempText
        activityWeatherBinding.nowInclude.currentSky.text = sky_1.info
        activityWeatherBinding.nowInclude.currentAQI.text = currentPM25Text
        activityWeatherBinding.nowInclude.nowLayout.setBackgroundResource(sky_1.bg)

        // 填充 forecast.xml 布局
        val days = daily.skycon.size
        activityWeatherBinding.forecastInclude.forecastLayout.removeAllViews()

        for (i in 0 until days) {
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, activityWeatherBinding.forecastInclude.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView

            val temperature = daily.temperature[i]
            val skycon = daily.skycon[i]
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sky = getSky(skycon.value)
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"

            dateInfo.text = simpleDateFormat.format(skycon.date)
            skyInfo.text = sky.info
            skyIcon.setImageResource(sky.icon)
            temperatureInfo.text = tempText

            activityWeatherBinding.forecastInclude.forecastLayout.addView(view)
        }

        // 填充 life_index.xml 布局
        val lifeIndex = daily.lifeIndex

        activityWeatherBinding.lifeIndexInclude.coldRiskText.text = lifeIndex.coldRisk[0].desc
        activityWeatherBinding.lifeIndexInclude.dressingText.text = lifeIndex.dressing[0].desc
        activityWeatherBinding.lifeIndexInclude.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        activityWeatherBinding.lifeIndexInclude.carWashingText.text = lifeIndex.carWashing[0].desc

        // 设置 ScrollView 可见
        activityWeatherBinding.weatherLayout.visibility = View.VISIBLE
    }
}