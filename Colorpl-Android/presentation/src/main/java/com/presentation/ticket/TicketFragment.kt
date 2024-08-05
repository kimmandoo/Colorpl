package com.presentation.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.presentation.base.BaseMapDialogFragment
import com.presentation.util.LocationHelper
import com.presentation.util.checkLocationPermission
import com.presentation.util.distanceChange
import com.presentation.util.ignoreParentScroll
import com.presentation.util.roadTimeChange
import com.presentation.util.setup
import com.presentation.viewmodel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TicketFragment : BaseMapDialogFragment<FragmentTicketBinding>(R.layout.fragment_ticket) {

    private val ticketViewModel: TicketViewModel by viewModels()
    override var mapView: MapView? = null
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    var currentLocation: LatLng? = null

    override fun initView(savedInstanceState: Bundle?) {
        initNaverMap()
        initUi()
        initClickEvent()
    }

    private fun initNaverMap() {
        this.mapView = binding.mapView
        locationSource = LocationHelper.getInstance().getFusedLocationSource()
        mapView?.getMapAsync(this@TicketFragment)
    }

    private fun initUi() {
        binding.apply {
            includePlace.tvTitleHint.text = getString(R.string.ticket_place)
            includeDate.tvTitleHint.text = getString(R.string.ticket_date)
            includeSeat.tvTitleHint.text = getString(R.string.ticket_seat)
            with(includeMyReview) {
                listOf(ivReport, ivComment, ivProfile, tvProfile, tvCommentCnt).forEach {
                    it.visibility = View.GONE
                }
            }

        }
    }

    private fun initClickEvent() {
        binding.apply {
            includeMyReview.clFeedDetail.setOnClickListener {
                findNavController().navigate(R.id.fragment_feed_detail)
            }
            tvFindRoad.setOnClickListener {
                ticketViewModel.getRoute(
                    LatLng(DEFAULT_LAT, DEFAULT_LONG)
                )
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMapReady(map: NaverMap) {
        val initMapView = mapView
        initMapView?.ignoreParentScroll()
        connectNaverMap(map)
        observePath(map)
    }

    private fun connectNaverMap(naverMap: NaverMap) {
        this.naverMap = naverMap.apply {
            setup(this@TicketFragment.locationSource)
        }
        checkLocationPermission(requireActivity())
        LocationHelper.getInstance().getClient().lastLocation.addOnSuccessListener { location ->
            ticketViewModel.setLatLng(LatLng(location.latitude, location.longitude))
            currentLocation?.let {
                naverMap.cameraPosition = CameraPosition(it, DEFAULT_ZOOM)
            }
        }
    }

    private fun observePath(naverMap: NaverMap) { // path 그리기
        ticketViewModel.routeData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { routeData ->
                val routeOverlay = PathOverlay()
                routeOverlay.apply {
                    coords = routeData.second
                    color = ContextCompat.getColor(binding.root.context, R.color.imperial_red)
                    outlineWidth = 4
                    map = naverMap
                }
                binding.apply {
                    totalTime = routeData.first.totalTime.roadTimeChange()
                    totalDistance = routeData.first.totalDistance.distanceChange()
                    this.clFindRoadContent.visibility = View.VISIBLE
                }
                val firstData = routeData.second.first()
                naverMap.cameraPosition = CameraPosition(firstData, 16.0)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        private const val DEFAULT_LAT = 36.0990913
        private const val DEFAULT_LONG = 128.4236401
        private const val DEFAULT_ZOOM = 15.0
    }
}