package com.domain.model

data class Route(
    val totalDistance: Int,
    val totalTime: Int,
    val totalWalkDistance: Int,
    val totalWalkTime: Int,
    val transferCount: Int,
    val totalFare: Int,
    val currency: String,
    val legs: List<Leg>
)