package com.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.colorpl.presentation.R
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.clustering.ClusterMarkerInfo
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.DefaultClusterMarkerUpdater
import com.naver.maps.map.clustering.DefaultLeafMarkerUpdater
import com.naver.maps.map.clustering.LeafMarkerInfo
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.presentation.map.model.MapMarker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/** Naver map 셋팅 */
fun NaverMap.setup(fusedLocationSource: FusedLocationSource) {
    mapType = NaverMap.MapType.Navi
    isNightModeEnabled = true
    locationSource = fusedLocationSource
    locationTrackingMode = LocationTrackingMode.Follow
    uiSettings.isZoomControlEnabled = false // Zoom 컨트롤러 사용유무.
    uiSettings.isLocationButtonEnabled = true // 현재위치 사용유무

}

/**
 * 내 위치 핀 오버레이의 특성값을 넣어 커스텀 셋팅
 * @param mainIconRes 메인 아이콘 이미지
 * @param subIconRes 서브 아이콘 이미지
 * */
fun LocationOverlay.setupOverlay(
    context: Context,
    mainIconRes: Int,
    mainIconWidth: Int = 40,
    mainIconHeight: Int = 40,
    subIconRes: Int?,
    subIconWidth: Int = 40,
    subIconHeight: Int = 40,
    circleRadius: Int = 50,
    circleColor: Int = ContextCompat.getColor(context, R.color.imperial_red),
    circleAlphaFactor: Float = 0.3f
) {
    apply {
        icon = OverlayImage.fromResource(mainIconRes)
        iconWidth = mainIconWidth
        iconHeight = mainIconHeight
        subIconRes?.let { subIcon ->
            this@setupOverlay.subIcon = OverlayImage.fromResource(subIcon)
            this@setupOverlay.subIconWidth = subIconWidth
            this@setupOverlay.subIconHeight = subIconHeight
        }
        this.circleRadius = circleRadius
        this.circleColor = circleColor.adjustAlpha(circleAlphaFactor)
    }
}

/**
 * 지도 스크롤이 부모 뷰의 스크롤에 영향 주지 않게 하는 메서드
 */
@SuppressLint("ClickableViewAccessibility")
fun MapView.ignoreParentScroll() {
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                this.parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        false
    }
}


fun makeMarker(
    marker: List<MapMarker>,
    builder: Clusterer.Builder<MapMarker>,
): Clusterer<MapMarker> { // cluster 연결
    val cluster: Clusterer<MapMarker> = builder.build()


    marker.forEach { item ->
        cluster.add(item, null)
    }

    return cluster
}

suspend fun deleteMarker(marker: Clusterer<MapMarker>) {
    withContext(Dispatchers.Default) {
        marker.map = null
    }
}

fun clickMarker(
    builder: Clusterer.Builder<MapMarker>,
    context: Context,
    markerInfo: (MapMarker) -> Unit?,
) {
    builder.clusterMarkerUpdater(object : DefaultClusterMarkerUpdater() {

        override fun updateClusterMarker(info: ClusterMarkerInfo, marker: Marker) {
            super.updateClusterMarker(info, marker)
            marker.apply {
                width = 100
                height = 100
            }
        }
    }).leafMarkerUpdater(object : DefaultLeafMarkerUpdater() {
        override fun updateLeafMarker(info: LeafMarkerInfo, marker: Marker) {
            super.updateLeafMarker(info, marker)
            marker.apply {
                width = 200
                height = 200
                val markerData = info.key as MapMarker
                icon = combineImages(context, R.drawable.ic_pin, R.drawable.dummy_ticket)
                onClickListener = Overlay.OnClickListener {

                    markerInfo(markerData)
                    true
                }
            }
        }
    })
}
