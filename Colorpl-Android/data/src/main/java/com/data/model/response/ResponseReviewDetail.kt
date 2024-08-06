package com.data.model.response

data class ResponseReviewDetail(
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
    val spoiler: Boolean,
    val ticketId: Int,
    val title: String,
    val writer: String
)