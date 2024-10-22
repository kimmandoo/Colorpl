package com.presentation.util

import android.util.Patterns

fun String.emailCheck(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Int.roadTimeChange(): String {
    val hours = this / (60 * 60)
    val minute = (this % (60 * 60)) / 60
    val text = if (hours > 0) {
        "${hours}시간 ${minute}분"
    } else {
        "${minute}분"
    }
    return text
}

fun Int.distanceChange(): String {
    val km = this / 1000
    val m = this % 1000 / 100
    val text = if (km > 0) {
        "${km}.${m}km "
    } else {
        "${m}m"
    }
    return text
}

fun Int.formatWithCommas(): String {
    return String.format("%,d", this)
}

