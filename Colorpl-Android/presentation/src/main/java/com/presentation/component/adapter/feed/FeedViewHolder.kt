package com.presentation.component.adapter.feed

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.colorpl.presentation.databinding.ItemFeedBinding
import com.domain.model.Feed

class FeedViewHolder(
    private val binding: ItemFeedBinding,
    private val onFeedContentClickListener: (Int) -> Unit,
    private val onCommentClickListener: () -> Unit,
    private val onEmotionClickListener: () -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,
) : ViewHolder(binding.root) {

    fun bind(data: Feed) {
        binding.apply {
            val clickScope = listOf(tvContent, tvTitle, ivContent, ivComment, tvCommentCnt)
            tvTitle.text = data.title
            tvContent.text = data.content
            tvEmotion.text = data.emotionMode.toString()
            tvProfile.text = data.writer
            tvCommentCnt.text = data.commentsCount.toString()
            tvUploadDate.text = data.createDate
            Glide.with(binding.root.context).load(data.contentImgUrl).centerCrop().into(ivContent)
            clickScope.forEach {
                it.setOnClickListener {
                    when (it) {
                        tvContent -> {
                            onFeedContentClickListener(data.id)
                        }
                    }
                }
            }
        }
    }
}