package com.presentation.util

import android.widget.TextView
import org.w3c.dom.Text
import java.util.regex.Pattern

fun String.emailCheck(): Boolean {
    val pattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\$"
    return Pattern.matches(pattern, this)
}