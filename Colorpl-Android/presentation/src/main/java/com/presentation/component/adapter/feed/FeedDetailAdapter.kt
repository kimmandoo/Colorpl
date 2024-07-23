package com.presentation.component.adapter.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemCommentBinding
import com.colorpl.presentation.databinding.ItemFeedDetailBinding
import com.domain.model.Comment
import com.domain.model.Feed

sealed class FeedDetail {
    data class HEADER(val feed: Feed) : FeedDetail()
    data class BODY(val comment: Comment) : FeedDetail()
}

class FeedDetailAdapter(
    private val onEditClickListener: () -> Unit,
    private val onDeleteClickListener: () -> Unit,
    private val onEmotionClickListener: () -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,
) : ListAdapter<FeedDetail, ViewHolder>(sealedDiffUtil) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedDetail.HEADER -> VIEW_TYPE_HEADER
            is FeedDetail.BODY -> VIEW_TYPE_BODY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemFeedDetailBinding.inflate(layoutInflater, parent, false)
                FeedDetailViewHolder(
                    binding,
                    onEmotionClickListener = onEmotionClickListener,
                    onReportClickListener = onReportClickListener,
                    onUserClickListener = onUserClickListener,
                )
            }

            VIEW_TYPE_BODY -> {
                val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
                CommentViewHolder(
                    binding,
                    onEditClickListener,
                    onDeleteClickListener
                )
            }

            else -> throw IllegalArgumentException("없는 ViewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val currentItem = getItem(position)) {
            is FeedDetail.HEADER -> (holder as FeedDetailViewHolder).bind(currentItem.feed)
            is FeedDetail.BODY -> (holder as CommentViewHolder).bind(currentItem.comment)
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_BODY = 1
        private val sealedDiffUtil = object : DiffUtil.ItemCallback<FeedDetail>() {
            override fun areItemsTheSame(oldItem: FeedDetail, newItem: FeedDetail): Boolean {
                return when {
                    oldItem is FeedDetail.HEADER && newItem is FeedDetail.HEADER ->
                        oldItem.feed.hashCode() == newItem.feed.hashCode()

                    oldItem is FeedDetail.BODY && newItem is FeedDetail.BODY ->
                        oldItem.comment.hashCode() == newItem.comment.hashCode()

                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: FeedDetail, newItem: FeedDetail): Boolean {
                return oldItem == newItem
            }
        }
    }
}