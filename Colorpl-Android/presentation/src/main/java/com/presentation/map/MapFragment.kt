package com.presentation.map

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : BaseMapFragment<FragmentMapBinding>(R.layout.fragment_map) {
    override var mapView: MapView? = null
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var locationOverlay: LocationOverlay
    private lateinit var markerBuilder: Clusterer.Builder<MapMarker>

    private var savedCameraPosition: CameraPosition? = null
    private val mapViewModel: MapViewModel by viewModels()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // NaverMap이 초기화된 경우에만 상태를 저장
        naverMap?.let { map ->
            outState.putParcelable("cameraPosition", map.cameraPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 카메라 위치 복원
        savedInstanceState?.let {
            savedCameraPosition = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("cameraPosition", CameraPosition::class.java)
            } else {
                it.getParcelable("cameraPosition")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // NaverMap 상태 복원 가능
        mapView?.getMapAsync { naverMap ->
            savedCameraPosition?.let {
                naverMap.cameraPosition = it
            }
        }
    }

    override fun initOnCreateView() {
        initNaverMap()
        showLoading()
    }

    override fun initOnMapReady(naverMap: NaverMap) {
        markerBuilder = Clusterer.Builder<MapMarker>()
        connectNaverMap(naverMap)
    }

    override fun iniViewCreated() {
        mapViewModel.getTicketList()
    }

    /** Naver map 초기 셋팅. */
    private fun initNaverMap() {
        mapView = binding.mvNaverMap
        locationSource = LocationHelper.getInstance().getFusedLocationSource()
        mapView?.getMapAsync(this)
    }

    /** Naver map 연결. */
    private fun connectNaverMap(naverMap: NaverMap) {
        this@MapFragment.naverMap = naverMap
        clickMarker(
            markerBuilder,
            requireActivity(),
            viewLifecycleOwner.lifecycleScope,
        ) { markerData ->
//            val action =
//                MapFragmentDirections.actionFragmentMapToFragmentTicket(markerData.toTicketResponse())
//            navigateDestination(action)
            Timber.d("markerData : $markerData")
        }
        observeTicketList()

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
    }

    private fun setMarker() {
        val markers = makeMarker(
            mapViewModel.ticketList.value.map {
                MapMarker(
                    id = it.id,
                    latitude = it.latitude ?: 0.0,
                    longitude = it.longitude ?: 0.0,
                    seat = it.seat,
                    dateTime = it.dateTime,
                    name = it.name,
                    category = it.category,
                    location = it.location ?: "",
                    imgUrl = it.imgUrl ?: "",
                    reviewExists = it.reviewExists,
                    reviewId = it.reviewId,
                )
            },
            markerBuilder
        )
        markers.map = naverMap
    }

    private fun observeTicketList() {
        Timber.tag(this::class.java.simpleName).d("티켓 리스트 관찰")
        mapViewModel.ticketList.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            Timber.d("이미지 확인 $it")
            setMarker()
            dismissLoading()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    companion object {

        private const val DEFAULT_ZOOM = 7.0
    }
}