package com.data.model.request

import kotlinx.serialization.Serializable

data class RequestTmapRoute(
    val count: Int = 1,
    val endX: String,
    val endY: String,
    val format: String = "json",
    val lang: Int = 0,
    val startX: String,
    val startY: String
)