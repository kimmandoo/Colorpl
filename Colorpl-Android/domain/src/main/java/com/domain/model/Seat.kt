package com.domain.model

data class Seat(
    val row: Int = -1,
    val col: Int = -1,
    val name: String = "",
    val grade: String? = null,
    val isReserved: Boolean = false,
    val isSelected: Boolean = false,
) {
    override fun toString(): String {
        return "${(row + 65).toChar()}${col + 1}"
    }
}
