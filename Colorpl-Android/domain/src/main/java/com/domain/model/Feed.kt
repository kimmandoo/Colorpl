package com.domain.model

data class Feed(
    val category: String = "",
    val commentpagesize: Int = 0,
    val commentscount: Int = 0,
    val content: String = "",
    val createdate: String = "",
    val emotion: Int = 0,
    val empathy: Int = 0,
    val id: Int = 0,
    val imgurl: String = "",
    val myempathy: Boolean = false,
    val myreview: Boolean = false,
    val scheduleId: Int = 0,
    val spoiler: Boolean = false,
    val title: String = "",
    val writer: String = ""
)