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
        return name
//        return "${(row + 65).toChar()}${col + 1}"
    }
    fun convertToHashMap() : MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["row"] = row
        map["col"] = col
        map["name"] = name
        grade?.let { map["grade"] = it }
        return map
    }

}
