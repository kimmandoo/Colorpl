package com.presentation.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.colorpl.presentation.R
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource


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
    context : Context,
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

