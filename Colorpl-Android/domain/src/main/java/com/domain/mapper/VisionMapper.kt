package com.domain.mapper

import com.data.model.response.ResponseVision
import com.domain.model.Description

fun ResponseVision.toEntity(): Description {
    val lines = this.choices.first().message.content.split("\n")

    fun processValue(value: String?): String? {
        return when {
            value.isNullOrBlank() -> null
            value.trim() == "없음" -> null
            else -> value.trim()
        }
    }

    return Description(
        title = processValue(lines.getOrNull(0)?.split(" ", limit = 2)?.last()).orEmpty(),
        detail = processValue(lines.getOrNull(1)?.split(" ", limit = 2)?.last()).orEmpty(),
        schedule = processValue(lines.getOrNull(2)?.split(" ", limit = 2)?.last()).orEmpty(),
        seat = processValue(lines.getOrNull(3)?.split(" ", limit = 3)?.last())
    )
}