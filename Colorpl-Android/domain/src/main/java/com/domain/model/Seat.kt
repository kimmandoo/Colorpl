package com.domain.model

data class Seat(
    val row: Int,
    val column: Int,
    val price: Int,
    val grade: Int,
    val isSelected: Boolean = false,
    val isReserved: Boolean = false,
){
    override fun toString(): String {
        return "${(row + 65).toChar()}${column + 1}"
    }
}
