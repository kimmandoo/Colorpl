package com.presentation.component.adapter.feed

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemFeedDetailBinding
import com.domain.model.Feed

class FeedDetailViewHolder(
    private val binding: ItemFeedDetailBinding,
    private val onEmotionClickListener: () -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,
) : ViewHolder(binding.root) {
    fun bind(data: Feed) {

    }
}