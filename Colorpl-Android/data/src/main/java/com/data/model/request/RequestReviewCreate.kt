package com.data.model.request

data class RequestReviewCreate(
    val memberId: Int,
    val ticketId: Int,
    val content: String,
    val spoiler: Boolean,
    val emotion: Int,
)
