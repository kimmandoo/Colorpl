package com.domain.model

import java.time.LocalDate

data class CalendarItem(
    val date: LocalDate,
    val isSunday: Boolean = false,
    val isCurrentMonth: Boolean,
    val isToday: Boolean = false,
    val isSelected: Boolean = false,
    val isWeek: Boolean = false,
    val imgUrl: String = "",
)
