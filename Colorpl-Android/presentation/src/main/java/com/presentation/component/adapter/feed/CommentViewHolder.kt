package com.presentation.component.adapter.feed

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemCommentBinding
import com.domain.model.Comment

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val onEditClickListener: () -> Unit,
    private val onDeleteClickListener: () -> Unit
) : ViewHolder(binding.root) {
    fun bind(data: Comment) {
        binding.apply {
            tvContent.text = data.commentContent
            tvName.text = data.writer
            tvUploadDate.text = data.createdate
        }
    }
}