package com.data.model.request

data class RequestCreateComment(
    val id: Int,
    val reviewId: Int,
    val memberId: Int,
    val writer: String,
    val commentContent: String,
    val createdate: String
)
