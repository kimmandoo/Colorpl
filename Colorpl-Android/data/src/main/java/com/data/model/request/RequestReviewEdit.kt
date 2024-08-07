package com.data.model.request

data class RequestReviewEdit(
    val content: String,
    val spoiler: Boolean,
    val emotion: Int
)