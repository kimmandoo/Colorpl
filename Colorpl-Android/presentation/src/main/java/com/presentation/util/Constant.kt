package com.presentation.util

import android.content.Context
import com.colorpl.presentation.R

enum class Ticket(val state: Int){
    ISSUED(0),
    UNISSUED(1)
}
enum class Calendar{
    CURRENT,
    NEXT,
    PREVIOUS,
    CHANGE
}

enum class FilterType(private val resourceId: Int) {
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

enum class Sign{
    ID,
    PASSWORD,
    NICKNAME,
    PROFILE
}

enum class Category{
    MOVIE,
    THEATRE,
    MUSICAL,
    CIRCUS,
    EXHIBITION
}