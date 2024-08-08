package com.presentation.component.adapter.feed

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.colorpl.presentation.databinding.ItemFeedBinding
import com.domain.model.Feed
import com.presentation.util.setImageCenterCrop
import timber.log.Timber

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
//            binding.img = data.imgurl
            tvTitle.text = data.title
            tvContent.text = data.content
            tvEmotion.text = data.emotion.toString()
            tvProfile.text = data.writer
            tvCommentCnt.text = data.commentscount.toString()
            tvUploadDate.text = data.createdate
            ivContent.setImageCenterCrop(data.imgurl)
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