package com.presentation.component.adapter.feed

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemCommentBinding
import com.domain.model.Comment

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val onEditClickListener: (Comment) -> Unit,
    private val onDeleteClickListener: (Int) -> Unit
) : ViewHolder(binding.root) {
    fun bind(data: Comment) {
        binding.apply {
            tvContent.text = data.commentContent
            tvName.text = data.writer
            tvUploadDate.text = data.createdate
            val ownerOptions = listOf(ivEdit, ivErase)
            ownerOptions.forEach { option ->
                option.visibility = if (!data.mycomment) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
            ivEdit.setOnClickListener {
                onEditClickListener(data)
            }
            ivErase.setOnClickListener {
                onDeleteClickListener(data.id)
            }
        }
    }
}