package com.data.model.paging

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    @SerialName("category")
    val category: String,
    @SerialName("commentpagesize")
    val commentpagesize: Int,
    @SerialName("commentscount")
    val commentscount: Int,
    @SerialName("content")
    val content: String,
    @SerialName("createdate")
    val createdate: String,
    @SerialName("emotion")
    val emotion: Int,
    @SerialName("empathy")
    val empathy: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("imgurl")
    val imgurl: String,
    @SerialName("myempathy")
    val myempathy: Boolean,
    @SerialName("myreview")
    val myreview: Boolean,
    @SerialName("spoiler")
    val spoiler: Boolean,
    @SerialName("ticketId")
    val ticketId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("writer")
    val writer: String
)