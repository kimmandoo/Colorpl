package com.presentation.map

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.presentation.base.BaseMapFragment
import com.presentation.map.model.MapMarker
import com.presentation.util.LocationHelper
import com.presentation.util.checkLocationPermission
import com.presentation.util.clickMarker
import com.presentation.util.makeMarker
import com.presentation.util.setup
import com.presentation.util.setupOverlay
import com.presentation.viewmodel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : BaseMapFragment<FragmentMapBinding>(R.layout.fragment_map) {
    override var mapView: MapView? = null
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var locationOverlay: LocationOverlay
    private lateinit var markerBuilder: Clusterer.Builder<MapMarker>
    private val mapViewModel: MapViewModel by viewModels()

    override fun initOnCreateView() {
        initNaverMap()
//        mapViewModel.getTicketList()
    }

    override fun initOnMapReady(naverMap: NaverMap) {
        connectNaverMap(naverMap)

    }

    override fun iniViewCreated() {

    }

    /** Naver map 초기 셋팅. */
    private fun initNaverMap() {
        mapView = binding.mvNaverMap
        locationSource = LocationHelper.getInstance().getFusedLocationSource()
        mapView?.getMapAsync(this)
    }

    /** Naver map 연결. */
    private fun connectNaverMap(naverMap: NaverMap) {
        markerBuilder = Clusterer.Builder<MapMarker>()
        this@MapFragment.naverMap = naverMap
        naverMap.setup(locationSource)
        this@MapFragment.locationOverlay = this@MapFragment.naverMap.locationOverlay
        this@MapFragment.locationOverlay.setupOverlay(
            context = binding.root.context,
            mainIconRes = R.drawable.ic_map_pin_main,
            subIconRes = null
        )
        checkLocationPermission(requireActivity())
        LocationHelper.getInstance().getClient().lastLocation.addOnSuccessListener { location ->
            this@MapFragment.naverMap.cameraPosition =
                CameraPosition(LatLng(location.latitude, location.longitude), DEFAULT_ZOOM)
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            clickMarker(markerBuilder, requireActivity()){ markerData ->
                Timber.d("markerData : $markerData")

            }
        }

        setMarker()
    }

    private fun setMarker() {
        val markers = makeMarker(
//            mapViewModel.ticketList.value,
            MapMarker.DEFAULT,
            markerBuilder
        )
        markers.map = naverMap
    }


    companion object {

        private const val DEFAULT_ZOOM = 15.0
    }
}