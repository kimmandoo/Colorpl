package com.data.model.paging


data class Feed(
    val category: String,
    val commentpagesize: Int,
    val commentscount: Int,
    val content: String,
    val createdate: String,
    val emotion: Int,
    val empathy: Int,
    val id: Int,
    val imgurl: String,
    val myempathy: Boolean,
    val myreview: Boolean,
    val scheduleId: Int,
    val spoiler: Boolean,
    val title: String,
    val writer: String
)