package com.presentation.util

import android.graphics.Color

/**
 * 색상의 투명도를 재조정 함.
 *
 * @param factor 조정할 투명도 : * 1.0f(100%) ~ 0.0f(0%)
 * @return 조정된 컬러값
 */
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).toInt()
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}