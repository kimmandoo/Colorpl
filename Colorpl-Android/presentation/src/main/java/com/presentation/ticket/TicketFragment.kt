package com.presentation.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.presentation.util.setEmotion
import com.presentation.util.setImageCenterCrop
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
    private val args: TicketFragmentArgs by navArgs()
    private val ticket by lazy { args.ticket }
    var currentLocation: LatLng? = null
    lateinit var action: NavDirections

    override fun initView(savedInstanceState: Bundle?) {
        initNaverMap()
        initUi()
        initClickEvent()
        observeReview()
    }

    private fun initNaverMap() {
        this.mapView = binding.mapView
        locationSource = LocationHelper.getInstance().getFusedLocationSource()
        mapView?.getMapAsync(this@TicketFragment)
    }

    private fun observeReview() {
        ticketViewModel.reviewDetail.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { reviewDetail ->
                action  = TicketFragmentDirections.actionFragmentTicketToFragmentFeedDetail(reviewId = reviewDetail.id)
                binding.apply {
                    includeMyReview.apply {
                        tvTitle.text = reviewDetail.title
                        tvProfile.text = reviewDetail.writer
                        tvCommentCnt.text = reviewDetail.commentCount.toString()
                        tvEmotion.text = reviewDetail.empathy.toString()
                        tvContent.text = reviewDetail.content
                        ivContent.setImageCenterCrop(reviewDetail.imgUrl)
                        ivEmotion.setEmotion(reviewDetail.emotion)
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initUi() {
        if (args.ticket.reviewId > 0) {
            ticketViewModel.getDetailReviewDetail(args.ticket.reviewId)
        }
        binding.apply {
            binding.titleText = ticket.name
            ivPoster.setImageCenterCrop(ticket.imgUrl)
            includeDate.tvTitle.text = ticket.dateTime
            includePlace.tvTitle.text = ticket.location
            includeSeat.tvTitle.text = ticket.seat
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
            val myReview = listOf(tvMyReview, includeMyReview.clFeedDetail)
            myReview.forEach {
                it.visibility = if (ticket.reviewExists) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            includeTop.ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            includeMyReview.clFeedDetail.setOnClickListener {
                if(::action.isInitialized){
                    navigateDestination(action)
                }
            }
            tvFindRoad.setOnClickListener {
                ticketViewModel.getRoute(
                    LatLng(
                        if (ticket.latitude == 0.0) DEFAULT_LAT else ticket.latitude,
                        if (ticket.longitude == 0.0) DEFAULT_LONG else ticket.longitude,
                    )
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
                    outlineWidth = 5
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