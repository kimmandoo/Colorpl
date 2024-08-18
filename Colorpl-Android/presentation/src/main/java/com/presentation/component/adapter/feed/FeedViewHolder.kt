package com.presentation.component.adapter.feed

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemFeedBinding
import com.domain.model.Feed
import com.presentation.util.setEmotion
import com.presentation.util.setImageCenterCrop
import timber.log.Timber

class FeedViewHolder(
    private val binding: ItemFeedBinding,
    private val onFeedContentClickListener: (Int) -> Unit,
    private val onEmotionClickListener: (Int, Boolean) -> Unit,
    private val onReportClickListener: () -> Unit,
    private val onUserClickListener: () -> Unit,
) : ViewHolder(binding.root) {

    fun bind(data: Feed) {
        binding.apply {
            Timber.d("데이터 확인여 ${data.myempathy}")
            val clickScope =
                listOf(tvContent, tvProfile, tvTitle, ivContent, ivComment, tvCommentCnt)
            tvTitle.text = data.title
            tvEmotion.text = data.empathy.toString()
            tvProfile.text = data.writer
            tvCommentCnt.text = data.commentscount.toString()
            tvUploadDate.text = data.createdate
            if (data.imgurl == "http://i11d109.p.ssafy.io/images/noimg.png") {
                ivContent.visibility = View.GONE
            } else {
                ivContent.setImageCenterCrop(data.imgurl)
            }
            tvContent.text = if (data.spoiler) {
                tvSpoiler.visibility = View.VISIBLE
                "스포일러가 포함된 리뷰입니다"
            } else {
                tvSpoiler.visibility = View.GONE
                data.content
            }
            clickScope.forEach {
                it.setOnClickListener { onFeedContentClickListener(data.id) }
            }
            ivEmotion.setEmotion(data.emotion)
            ivEmotion.isSelected = data.myempathy
            ivEmotion.setOnClickListener {
                onEmotionClickListener(data.id, data.myempathy)
            }
        }
    }
}