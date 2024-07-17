package com.data.model.response

data class ResponseMarkerData(
    val `data`: List<Data>
) {
    data class Data(
        val id: Int,
        val latitude: Int,
        val longitude: Int,
        val postId: Int
    )
}