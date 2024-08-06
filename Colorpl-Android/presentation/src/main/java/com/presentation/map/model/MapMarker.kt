package com.presentation.map.model

import android.graphics.drawable.Drawable
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.clustering.ClusteringKey

data class MapMarker(
    val latitude : Double,
    val longitude : Double,
) : ClusteringKey{
    override fun getPosition(): LatLng = LatLng(latitude, longitude)

    companion object{
        val DEFAULT = listOf(MapMarker(36.0990913, 128.4236401),
            MapMarker(36.10456406936184, 128.41981348179178),
            MapMarker(36.10324329683452,128.42339515686035 ),
            MapMarker(36.10324647862417, 128.41663774631814),
            MapMarker(36.106609758558214,128.4262436308565 )
        )
    }
}


