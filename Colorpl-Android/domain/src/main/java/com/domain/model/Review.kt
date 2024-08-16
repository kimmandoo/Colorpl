package com.domain.model

data class Review(
    val scheduleId: Int,
    val content: String,
    val spoiler: Boolean,
    val emotion: Int,
)
