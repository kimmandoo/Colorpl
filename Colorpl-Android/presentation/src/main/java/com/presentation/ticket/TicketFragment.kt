package com.presentation.ticket

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import com.presentation.base.BaseMapDialogFragment
import com.presentation.util.ignoreParentScroll
import com.presentation.viewmodel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TicketFragment : BaseMapDialogFragment<FragmentTicketBinding>(R.layout.fragment_ticket) {

    private val viewModel: TicketViewModel by viewModels()
    override var mapView: MapView? = null

    override fun initView(savedInstanceState: Bundle?) {
        initUi(savedInstanceState)
    }

    private fun initUi(savedInstanceState: Bundle?) {
        binding.apply {
            includePlace.tvTitleHint.text = getString(R.string.ticket_place)
            includeDate.tvTitleHint.text = getString(R.string.ticket_date)
            includeSeat.tvTitleHint.text = getString(R.string.ticket_seat)
            with(includeMyReview) {
                listOf(ivReport, ivComment, ivProfile, tvProfile, tvCommentCnt).forEach {
                    it.visibility = View.GONE
                }
            }
            includeMyReview.clFeedDetail.setOnClickListener {
                findNavController().navigate(R.id.fragment_feed_detail)
            }
            tvFindRoad.setOnClickListener {
                viewModel.getRoute(
                    LatLng(37.63788539420793, 127.02550910860451),
                    LatLng(37.609094989686, 127.030406594109)
                )
            }
        }

        this@TicketFragment.mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this@TicketFragment)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMapReady(map: NaverMap) {
        val initMapView = mapView!!
        initMapView.ignoreParentScroll()
        observePath(map)
        map.apply {
            mapType = NaverMap.MapType.Navi
            isNightModeEnabled = true
        }
    }

    private fun observePath(naverMap: NaverMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.routeData.collectLatest { routeData ->
                    val routeOverlay = PathOverlay()
                    routeOverlay.apply {
                        coords = routeData
                        color = ContextCompat.getColor(binding.root.context, R.color.imperial_red)
                        outlineWidth = 1
                        map = naverMap
                    }
                }
            }
        }
    }
}