package com.data.model.request

data class RequestReviewCreate(
    val scheduleId: Int,
    val content: String,
    val spoiler: Boolean,
    val emotion: Int,
)
