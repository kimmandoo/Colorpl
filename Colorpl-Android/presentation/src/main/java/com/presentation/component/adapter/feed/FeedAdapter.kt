package com.presentation.component.adapter.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemFeedBinding
import com.domain.model.Feed
import com.presentation.base.BaseDiffUtil

class FeedAdapter(
    private val onFeedContentClickListener: (Int) -> Unit,
    private val onEmotionClickListener: (Int, Boolean) -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,

    ) : PagingDataAdapter<Feed, ViewHolder>(feedCustomDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedBinding.inflate(layoutInflater)
        return FeedViewHolder(
            binding,
            onFeedContentClickListener = onFeedContentClickListener,
            onEmotionClickListener = onEmotionClickListener,
            onReportClickListener = onReportClickListener,
            onUserClickListener = onUserClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as? Bundle
            if (bundle != null) {
                (holder as FeedViewHolder).updateEmpathy(
                    bundle.getBoolean("myempathy"),
                    bundle.getInt("empathy")
                )
            } else {
                super.onBindViewHolder(holder, position, payloads)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is FeedViewHolder -> {
                val item = getItem(position)
                item?.let {
                    holder.bind(item)
                }
            }
        }
    }

    companion object {
        private val feedCustomDiffUtil = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: Feed, newItem: Feed): Any? {
                return if (oldItem.myempathy != newItem.myempathy || oldItem.empathy != newItem.empathy) {
                    Bundle().apply {
                        putBoolean("myempathy", newItem.myempathy)
                        putInt("empathy", newItem.empathy)
                    }
                } else null
            }
        }
    }
}