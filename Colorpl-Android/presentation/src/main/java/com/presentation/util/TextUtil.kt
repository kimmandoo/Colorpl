package com.presentation.util

import android.util.Patterns
import android.widget.TextView
import org.w3c.dom.Text
import java.util.regex.Pattern

fun String.emailCheck(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}