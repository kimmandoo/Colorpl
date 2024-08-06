package com.data.model.paging

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    @SerialName("category")
    val category: String,
    @SerialName("commentpagesize")
    val commentPageSize: Int,
    @SerialName("commentscount")
    val commentsCount: Int,
    @SerialName("content")
    val content: String,
    @SerialName("createdate")
    val createDate: String,
    @SerialName("emotion")
    val emotionMode: Int,
    @SerialName("empathy")
    val empathyCount: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("imgurl")
    val contentImgUrl: String,
    @SerialName("myempathy")
    val isEmpathy: Boolean,
    @SerialName("myreview")
    val isMyReview: Boolean,
    @SerialName("spoiler")
    val isSpoiler: Boolean,
    @SerialName("ticketId")
    val ticketId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("writer")
    val writer: String
)