package com.domain.model

data class ReviewDetail(
    val category: String ="",
    val commentPageSize: Int=0,
    val commentCount: Int=0,
    val content: String="",
    val createDate: String="",
    val emotion: Int=0,
    val empathy: Int=0,
    val id: Int=0,
    val imgUrl: String="",
    val myEmpathy: Boolean=false,
    val myReview: Boolean=false,
    val spoiler: Boolean=false,
    val ticketId: Int=0,
    val title: String="",
    val writer: String=""
)
