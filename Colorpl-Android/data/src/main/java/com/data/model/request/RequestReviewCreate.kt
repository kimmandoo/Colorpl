package com.data.model.request

data class RequestReviewCreate(
    val ticketId: Int,
    val content: String,
    val spoiler: Boolean,
    val emotion: Int,
)
