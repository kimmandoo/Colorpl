package com.presentation.util

import android.content.Context
import com.colorpl.presentation.R
import com.domain.model.FilterItem

enum class FilterType(val resourceId: Int) {
    ALL(R.string.feed_filter_all),
    MOVIE(R.string.feed_filter_movie),
    PERFORMANCE(R.string.feed_filter_performance),
    CONCERT(R.string.feed_filter_concert),
    PLAY(R.string.feed_filter_play),
    MUSICAL(R.string.feed_filter_musical),
    EXHIBITION(R.string.feed_filter_exhibition);

    fun getText(context: Context): String {
        return context.getString(resourceId)
    }
}

fun Context.getFilterItems(): List<FilterItem> {
    return FilterType.entries.toTypedArray().mapIndexed { index, type ->
        FilterItem(type.getText(this), index == 0)
    }
}