package com.presentation.map

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMapBinding
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.presentation.base.BaseMapFragment

class MapFragment: BaseMapFragment<FragmentMapBinding>(R.layout.fragment_map) {
    override var mapView: MapView? = null

    override fun initOnCreateView() {

    }

    override fun initOnMapReady(naverMap: NaverMap) {

    }

    override fun iniViewCreated() {

    }
}