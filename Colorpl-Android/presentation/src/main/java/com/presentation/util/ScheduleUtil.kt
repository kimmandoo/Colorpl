package com.presentation.util

import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun RecyclerView.overScrollControl(logic: (Int, Float) -> Unit) {
    edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(
            recyclerView: RecyclerView,
            direction: Int
        ): EdgeEffect {
            return object : EdgeEffect(recyclerView.context) {
                override fun onPull(deltaDistance: Float) {
                    super.onPull(deltaDistance)
                    logic(direction, deltaDistance)
                }

                override fun onPull(deltaDistance: Float, displacement: Float) {
                    super.onPull(deltaDistance, displacement)
                    logic(direction, deltaDistance)
                }
            }
        }
    }
}

fun LocalDate.getPattern(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return LocalDateTime.parse(this, formatter).toLocalDate()
}
