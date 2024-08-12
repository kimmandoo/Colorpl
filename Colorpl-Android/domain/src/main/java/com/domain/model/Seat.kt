package com.domain.model

import java.io.Serializable

data class Seat(
    val row: Int = -1,
    val col: Int = -1,
    val name: String = "",
    val grade: String? = null,
    val isReserved: Boolean = false,
    val isSelected: Boolean = false,
) : Serializable {
    override fun toString(): String {
        return "$name:$row:$col"
//        return "${(row + 65).toChar()}${col + 1}"
    }

}
