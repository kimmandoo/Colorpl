package com.domain.model

import java.util.Date

data class Comment(
    val id: Int,
    val reviewId: Int,
    val memberId: Int,
    val writer: String,
    val commentContent: String,
    val createdate: String,
)
