package com.presentation.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketBinding
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.presentation.base.BaseMapDialogFragment
import com.presentation.util.ignoreParentScroll
import com.presentation.viewmodel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                viewModel.getRoute()
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
        map.apply {
            mapType = NaverMap.MapType.Navi
            isNightModeEnabled = true
        }
    }
}