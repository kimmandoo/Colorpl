package com.data.model.paging.reservation



data class Show(
    val id: Int,
    val apiId: String?,
    val name: String,
    val cast: String?,
    val runtime: String,
    val priceBySeatClass: Map<String, Int>,
    val posterImagePath: String,
    val area: String,
    val category: String?,
    val state: String?,
    val seats: List<Seat>?,
    val hall: String?,
    val schedule: Map<String, Boolean>?,
)

data class Seat(
    val row: Int,
    val col: Int,
    val grade: String,
)