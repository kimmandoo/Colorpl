package com.data.model.paging

import java.util.Date

data class Feed(
    val feedId: Int,
    val title: String,
    val userName: String,
    val userProfileImg: String?,
    val contentImg: String?,
    val emotionMode: String,
    val emotionTotal: Int,
    val commentTotal: Int,
    val uploadedDate: Date
)
