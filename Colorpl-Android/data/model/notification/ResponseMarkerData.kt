package com.data.model.notification

data class ResponseMarkerData(
    val data : List<Data>
) {
    data class Data(
        val id: Int,
        val latitude: Int,
        val longitude: Int,
        val postId: Int
    )
}