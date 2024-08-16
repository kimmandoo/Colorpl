package com.presentation.component.adapter.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemCommentBinding
import com.domain.model.Comment
import com.presentation.base.BaseDiffUtil

class CommentAdapter(
    private val onEditClickListener: (Comment) -> Unit,
    private val onDeleteClickListener: (Int) -> Unit
) : PagingDataAdapter<Comment, ViewHolder>(BaseDiffUtil<Comment>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
        return CommentViewHolder(
            binding,
            onEditClickListener,
            onDeleteClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as CommentViewHolder).bind(item)
        }
    }
}