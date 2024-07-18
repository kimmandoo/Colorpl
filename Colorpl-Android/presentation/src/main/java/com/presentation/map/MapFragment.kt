package com.presentation.map

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMapBinding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.presentation.base.BaseMapFragment

class MapFragment: BaseMapFragment<FragmentMapBinding>(R.layout.fragment_map) {
    override var mapView: MapView? = null
    private val mainActivity by lazy {
        requireContext()
    }
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var locationOverlay: LocationOverlay
    override fun initOnCreateView() {
        mapView = binding.mvNaverMap
        mapView?.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

    }

    override fun initOnMapReady(naverMap: NaverMap) {
        this@MapFragment.naverMap = naverMap
        naverMap.setup()
        this@MapFragment.locationOverlay = this@MapFragment.naverMap.locationOverlay
        this@MapFragment.locationOverlay.setupOverlay(mainIconRes = R.drawable.ic_map_pin_main, subIconRes = null)
    }

    override fun iniViewCreated() {

    }

    /** Naver map 셋팅 */
    private fun NaverMap.setup() {
        this.mapType = NaverMap.MapType.Navi
        this.isNightModeEnabled = true
        this.locationSource = this@MapFragment.locationSource
        this.locationTrackingMode = LocationTrackingMode.Follow
        this.uiSettings.isZoomControlEnabled = false // Zoom 컨트롤러 사용유무.
        this.uiSettings.isLocationButtonEnabled = true // 현재위치 사용유무.
    }

    /**
     * 내 위치 핀 오버레이의 특성값을 넣어 커스텀 셋팅
     * @param mainIconRes 메인 아이콘 이미지
     * @param subIconRes 서브 아이콘 이미지
     * */
    private fun LocationOverlay.setupOverlay(
        mainIconRes: Int,
        mainIconWidth: Int = 40,
        mainIconHeight: Int = 40,
        subIconRes: Int?,
        subIconWidth: Int = 40,
        subIconHeight: Int = 40,
        circleRadius: Int = 50,
        circleColor: Int = ContextCompat.getColor(mainActivity, R.color.imperial_red),
        circleAlphaFactor: Float = 0.3f
    ) {
        this.let {
            it.icon = OverlayImage.fromResource(mainIconRes)
            it.iconWidth = mainIconWidth
            it.iconHeight = mainIconHeight
            subIconRes.apply {
                it.subIcon = subIconRes?.let { it1 -> OverlayImage.fromResource(it1) }
                it.subIconWidth = subIconWidth
                it.subIconHeight = subIconHeight
            }

            it.circleRadius = circleRadius
            it.circleColor = circleColor.adjustAlpha(circleAlphaFactor)
        }
    }

    /**
     * 색상의 투명도를 재조정 함.
     *
     * @param factor 조정할 투명도 : * 1.0f(100%) ~ 0.0f(0%)
     * @return 조정된 컬러값
     */
    private fun Int.adjustAlpha(factor: Float): Int {
        val alpha = (Color.alpha(this) * factor).toInt()
        return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}