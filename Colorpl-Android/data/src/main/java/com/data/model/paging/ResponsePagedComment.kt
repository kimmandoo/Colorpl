package com.data.model.paging

data class ResponsePagedComment(
    val items: List<Comment>,
    val totalPages: Int,
)