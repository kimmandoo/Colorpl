package com.presentation.component.adapter.feed

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemFeedBinding
import com.domain.model.Feed
import com.presentation.util.setEmotion
import com.presentation.util.setImageCenterCrop

class FeedViewHolder(
    private val binding: ItemFeedBinding,
    private val onFeedContentClickListener: (Int) -> Unit,
    private val onEmotionClickListener: (Int, Boolean) -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,
) : ViewHolder(binding.root) {

    fun bind(data: Feed) {
        binding.apply {
            val clickScope = listOf(tvContent, tvTitle, ivContent, ivComment, tvCommentCnt)
            tvTitle.text = data.title
            tvContent.text = data.content
            tvEmotion.text = data.empathy.toString()
            tvProfile.text = data.writer
            tvCommentCnt.text = data.commentscount.toString()
            tvUploadDate.text = data.createdate
            ivEmotion.setEmotion(data.emotion)
            updateEmpathy(data.myempathy, data.empathy)
            ivContent.setImageCenterCrop(data.imgurl)
            clickScope.forEach {
                it.setOnClickListener { onFeedContentClickListener(data.id) }
            }
            ivEmotion.setOnClickListener {
                onEmotionClickListener(data.id, data.myempathy)
            }
        }
    }

    fun updateEmpathy(myEmpathy: Boolean, empathyCount: Int) {
        binding.apply {
            ivEmotion.isSelected = myEmpathy
            tvEmotion.text = empathyCount.toString()
        }
    }
}