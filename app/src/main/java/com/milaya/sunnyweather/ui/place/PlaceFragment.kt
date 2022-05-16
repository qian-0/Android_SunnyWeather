package com.milaya.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.milaya.sunnyweather.MainActivity
import com.milaya.sunnyweather.databinding.FragmentPlaceBinding
import com.milaya.sunnyweather.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {
//    视图绑定
    private var _fragmentPlaceBinding: FragmentPlaceBinding? = null
    private val fragmentPlaceBinding get() = _fragmentPlaceBinding!!
//    获得 PlaceViewModel 实例
    val viewModel by lazy {
        ViewModelProviders.of(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter

//    加载 fragment_place 布局
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentPlaceBinding = FragmentPlaceBinding.inflate(inflater, container, false)
        val view = fragmentPlaceBinding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (/*activity is MainActivity && */viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

//        指定 RecycleView 布局方式
        val layoutManager = LinearLayoutManager(activity)
        fragmentPlaceBinding.recyclerView.layoutManager = layoutManager
//        设置 RecycleView 适配器
        adapter = PlaceAdapter(this, viewModel.placeList)
        fragmentPlaceBinding.recyclerView.adapter = adapter

        fragmentPlaceBinding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                fragmentPlaceBinding.recyclerView.visibility = View.GONE
                fragmentPlaceBinding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer{ result ->
            val places = result.getOrNull()
            if (places != null) {
                fragmentPlaceBinding.recyclerView.visibility = View.VISIBLE
                fragmentPlaceBinding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentPlaceBinding = null
    }
}