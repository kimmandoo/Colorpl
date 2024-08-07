package com.data.model.request

data class RequestReviewEdit(
    val scheduleId: Int,
    val content: String,
    val spoiler: Boolean,
    val emotion: String
)