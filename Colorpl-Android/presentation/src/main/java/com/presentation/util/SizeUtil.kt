package com.presentation.util

import android.content.res.Resources
import android.util.TypedValue

private val displayMetrics
    get() = Resources.getSystem().displayMetrics



val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


fun dpToPxF(dp: Int): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics)


fun dpToPx(dp: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics).toInt()

val Number.dpToPx: Int
    get() = dpToPx(this.toInt())