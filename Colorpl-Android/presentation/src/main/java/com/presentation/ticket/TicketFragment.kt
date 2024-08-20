package com.presentation.ticket

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
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
import com.naver.maps.map.overlay.MultipartPathOverlay
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.presentation.base.BaseMapDialogFragment
import com.presentation.util.LocationHelper
import com.presentation.util.checkLocationPermission
import com.presentation.util.distanceChange
import com.presentation.util.formatIsoToKorean
import com.presentation.util.ignoreParentScroll
import com.presentation.util.roadTimeChange
import com.presentation.util.setEmotion
import com.presentation.util.setImageCenterCrop
import com.presentation.util.setup
import com.presentation.viewmodel.ScheduleViewModel
import com.presentation.viewmodel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TicketFragment : BaseMapDialogFragment<FragmentTicketBinding>(R.layout.fragment_ticket) {

    private val ticketViewModel: TicketViewModel by viewModels()
    private val scheduleViewModel: ScheduleViewModel by activityViewModels()
    override var mapView: MapView? = null
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val args: TicketFragmentArgs by navArgs()
    private val ticketId by lazy { args.ticket }
    private var currentLocation: LatLng? = null
    lateinit var action: NavDirections

    override fun initView(savedInstanceState: Bundle?) {
        initNaverMap()
        initUi()
        initClickEvent()
        observeSingleTicket()
        observeReview()
        observeDialog()
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("closed", true)
        super.onDismiss(dialog)
    }

    private fun initNaverMap() {
        this.mapView = binding.mapView
        locationSource = LocationHelper.getInstance().getFusedLocationSource()
        mapView?.getMapAsync(this@TicketFragment)
    }

    private fun observeReview() {
        ticketViewModel.reviewDetail.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { reviewDetail ->
                action =
                    TicketFragmentDirections.actionFragmentTicketToFragmentFeedDetail(reviewId = reviewDetail.id)
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

    private fun observeSingleTicket() {
        ticketViewModel.ticketData.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            dismissLoading()
            val ticket = it
            if (it.reviewId > 0) {
                ticketViewModel.getDetailReviewDetail(it.reviewId)
            }
            if (it.type == "CUSTOM") {
                listOf(binding.tvEdit, binding.tvDelete).forEach { view ->
                    view.visibility = View.VISIBLE
                }
                binding.tvEdit.setOnClickListener {
                    val action =
                        TicketFragmentDirections.actionFragmentTicketToNavTicketUpdateGraph(ticketId)
                    findNavController().navigate(action)
                }
                binding.tvDelete.setOnClickListener {
                    showLoading()
                    ticketViewModel.deleteTicket(ticket.id)
                }
            }
            binding.apply {
                binding.titleText = ticket.name
                ivPoster.setImageCenterCrop(ticket.imgUrl)
                includeDate.tvTitle.text = ticket.dateTime.formatIsoToKorean()
                includePlace.tvTitle.text = ticket.location
                includeSeat.tvTitle.text = ticket.seat
                includePlace.tvTitleHint.text = getString(R.string.ticket_place)
                includeDate.tvTitleHint.text = getString(R.string.ticket_date)
                includeSeat.tvTitleHint.text = getString(R.string.ticket_seat)
                with(includeMyReview) {
                    listOf(ivReport, ivComment, tvProfile, tvCommentCnt).forEach {
                        it.visibility = View.GONE
                    }
                }

                val myReview = listOf(tvMyReview, includeMyReview.clFeedDetail)
                myReview.forEach {
                    it.visibility = if (ticket.reviewExists) {
                        View.VISIBLE
                    } else {
                        View.GONE
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
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        ticketViewModel.ticketDeleteResponse.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it) {
                    scheduleViewModel.refreshTickets()
                    Toast.makeText(requireContext(), "티켓이 삭제되었습니다", Toast.LENGTH_SHORT).show()
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "closed",
                        true
                    )
                    dismissLoading()
                    findNavController().popBackStack()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initUi() {
        showLoading()
        ticketViewModel.getSingleTicket(ticketId)
    }

    private fun initClickEvent() {
        binding.apply {
            includeTop.ivBack.setOnClickListener {
                scheduleViewModel.refreshTickets()
                findNavController().previousBackStackEntry?.savedStateHandle?.set("closed", true)
                findNavController().popBackStack()
            }
            includeMyReview.clFeedDetail.setOnClickListener {
                if (::action.isInitialized) {
                    navigateDestination(action)
                }
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
        ticketViewModel.isRouteNull.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                if (state) {
                    Toast.makeText(requireContext(), "경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        ticketViewModel.routeData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { routeData ->
                routeData.second.forEach { segment ->
                    val pathOverlay = PathOverlay()
                    pathOverlay.apply {
                        coords = segment.map { it.route }
                        color = when (segment.first().mode) {
                            0 -> ContextCompat.getColor(requireContext(), R.color.light_gray) // 도보
                            1 -> ContextCompat.getColor(requireContext(), R.color.orange) // 버스나 지하철
                            2 -> ContextCompat.getColor(requireContext(), R.color.imperial_red) // 고속버스
                            else -> ContextCompat.getColor(requireContext(), R.color.orange) // 그 외
                        }
                        outlineWidth = 5
                        map = naverMap
                    }
                }
                val firstData = routeData.second.first().first().route
                naverMap.cameraPosition = CameraPosition(firstData, 14.0)

                binding.apply {
                    totalTime = routeData.first.totalTime.roadTimeChange()
                    totalDistance = routeData.first.totalDistance.distanceChange()
                    this.clFindRoadContent.visibility = View.VISIBLE
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun observeDialog() {
        viewLifecycleOwner.lifecycleScope.launch {
            findNavController()
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow<Boolean>("closed", false)
                ?.collectLatest { closed ->
                    if (closed) {
                        scheduleViewModel.refreshTickets()
                        ticketViewModel.getSingleTicket(args.ticket)
                        findNavController().currentBackStackEntry?.savedStateHandle?.set(
                            "closed",
                            false
                        )
                    }
                }
        }
    }

    companion object {
        private const val DEFAULT_LAT = 36.0990913
        private const val DEFAULT_LONG = 128.4236401
        private const val DEFAULT_ZOOM = 15.0
    }
}