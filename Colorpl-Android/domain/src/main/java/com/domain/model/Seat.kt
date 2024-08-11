package com.domain.model

data class Seat(
    val row: Int,
    val col: Int,
    val name: String,
    val grade: String?,
    val isReserved: Boolean = false,
    val isSelected: Boolean = false,
) {
    override fun toString(): String {
        return "${(row + 65).toChar()}${col + 1}"
    }
}
