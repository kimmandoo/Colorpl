package com.presentation.util

import android.content.Context
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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

fun stringToCalendar(dateString: String): Calendar? {
    val format = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.getDefault())

    return runCatching {
        val date = format.parse(dateString) ?: throw IllegalArgumentException("날짜가 null")
        Calendar.getInstance().apply {
            time = date
        }
    }.getOrNull()
}

fun hourToMills(hour : Int) : Int{
    return hour * 60 * 60 * 1000
}

fun minToMills(min : Int) : Int{
    return min * 60 * 1000
}