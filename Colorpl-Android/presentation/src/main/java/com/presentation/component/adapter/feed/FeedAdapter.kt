package com.presentation.component.adapter.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemFeedBinding
import com.domain.model.Feed
import com.presentation.base.BaseDiffUtil

class FeedAdapter(
    private val onFeedContentClickListener: () -> Unit,
    private val onCommentClickListener: () -> Unit,
    private val onEmotionClickListener: () -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,
) : ListAdapter<Feed, ViewHolder>(BaseDiffUtil<Feed>()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedBinding.inflate(layoutInflater)
        return FeedViewHolder(
            binding,
            onFeedContentClickListener = onFeedContentClickListener,
            onCommentClickListener = onCommentClickListener,
            onEmotionClickListener = onEmotionClickListener,
            onReportClickListener = onReportClickListener,
            onUserClickListener = onUserClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is FeedViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }
}