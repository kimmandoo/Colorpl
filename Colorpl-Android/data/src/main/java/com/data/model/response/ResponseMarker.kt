package com.data.model.response

data class ResponseMarker(
    val `data`: List<Data>
) {
    data class Data(
        val id: Int,
        val latitude: Int,
        val longitude: Int,
        val postId: Int
    )
}