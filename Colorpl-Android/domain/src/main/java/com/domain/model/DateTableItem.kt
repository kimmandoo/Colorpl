package com.domain.model

import java.time.LocalDate

data class DateTableItem(
    val date: LocalDate,
    var isSelected: Boolean = false,
    val isEvent: Boolean = false,
)
