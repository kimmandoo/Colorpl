package com.presentation.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketBinding
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.presentation.base.BaseDialogFragment
import com.presentation.base.BaseMapDialogFragment
import com.presentation.util.ignoreParentScroll

class TicketFragment : BaseMapDialogFragment<FragmentTicketBinding>(R.layout.fragment_ticket){

    override var mapView: MapView? = null

    override fun initView(savedInstanceState: Bundle?) {
        initUi(savedInstanceState)
    }

    private fun initUi(savedInstanceState: Bundle?) {
        binding.apply {
            includePlace.tvTitleHint.text = "장소"
            includeDate.tvTitleHint.text = "일시"
            includeSeat.tvTitleHint.text = "좌석"
            includeMyReview.ivReport.visibility = View.GONE
            includeMyReview.ivComment.visibility = View.GONE
            includeMyReview.ivProfile.visibility = View.GONE
            includeMyReview.tvProfile.visibility = View.GONE
            includeMyReview.tvCommentCnt.visibility = View.GONE
            includeMyReview.ivReport.visibility = View.GONE
        }

        this@TicketFragment.mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this@TicketFragment)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMapReady(map: NaverMap) {
        val initMapView = mapView!!
        initMapView.ignoreParentScroll()
    }
}