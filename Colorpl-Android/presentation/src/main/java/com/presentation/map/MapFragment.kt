package com.presentation.map

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMapBinding
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.presentation.base.BaseMapFragment
import com.presentation.util.setup
import com.presentation.util.setupOverlay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment: BaseMapFragment<FragmentMapBinding>(R.layout.fragment_map) {
    override var mapView: MapView? = null
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var locationOverlay: LocationOverlay
    override fun initOnCreateView() {
        initNaverMap()
    }

    override fun initOnMapReady(naverMap: NaverMap) {
        connectNaverMap(naverMap)
    }

    override fun iniViewCreated() {

    }

    /** Naver map 초기 셋팅. */
    private fun initNaverMap(){
        mapView = binding.mvNaverMap
        mapView?.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    /** Naver map 연결. */
    private fun connectNaverMap(naverMap: NaverMap) {
        this@MapFragment.naverMap = naverMap
        naverMap.setup(locationSource)
        this@MapFragment.locationOverlay = this@MapFragment.naverMap.locationOverlay
        this@MapFragment.locationOverlay.setupOverlay(context = binding.root.context, mainIconRes = R.drawable.ic_map_pin_main, subIconRes = null)
    }




    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}