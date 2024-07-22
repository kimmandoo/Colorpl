package com.presentation.component.adapter.feed

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemFeedBinding
import com.domain.model.Feed

class FeedViewHolder(
    private val binding: ItemFeedBinding,
    private val onFeedContentClickListener: () -> Unit,
    private val onCommentClickListener: () -> Unit,
    private val onEmotionClickListener: () -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,
) : ViewHolder(binding.root) {

    fun bind(data: Feed) {

    }
}