package com.data.model.paging

data class ResponsePagedFeed(
    val items: List<Feed>,
    val totalPages: Int,
    val currentPage: Int
)