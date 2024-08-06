package com.domain.model

import java.util.Date

data class Feed(
    val category: String,
    val commentPageSize: Int,
    val commentsCount: Int,
    val content: String,
    val createDate: String,
    val emotionMode: Int,
    val empathyCount: Int,
    val id: Int,
    val contentImgUrl: String,
    val isEmpathy: Boolean,
    val isMyReview: Boolean,
    val isSpoiler: Boolean,
    val ticketId: Int,
    val title: String,
    val writer: String
)