package com.presentation.util

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.R
import com.domain.model.FilterItem

fun Context.getFilterItems(): List<FilterItem> {
    return FilterType.entries.toTypedArray().mapIndexed { index, type ->
        FilterItem(type.getText(this), index == 0)
    }
}

fun ImageView.setEmotion(emotion: Int){
    setImageResource(
        when (emotion) {
            1 -> R.drawable.selector_ic_excited
            2 -> R.drawable.selector_ic_love
            3 -> R.drawable.selector_ic_tired
            4 -> R.drawable.selector_ic_crying
            5 -> R.drawable.selector_ic_angry
            else -> R.drawable.selector_ic_excited
        }
    )
}