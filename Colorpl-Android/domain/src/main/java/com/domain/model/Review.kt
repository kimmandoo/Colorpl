package com.domain.model

data class Review(
    val memberId: Int,
    val ticketId: Int,
    val content: String,
    val spoiler: Boolean,
    val emotion: Int,
)
