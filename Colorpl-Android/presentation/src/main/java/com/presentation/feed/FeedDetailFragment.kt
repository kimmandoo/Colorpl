package com.presentation.feed

import android.os.Bundle
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedDetailBinding
import com.domain.model.Comment
import com.domain.model.Feed
import com.presentation.base.BaseDialogFragment
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FeedDetail
import com.presentation.component.adapter.feed.FeedDetailAdapter
import java.util.Date

class FeedDetailFragment : BaseDialogFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail) {

    private val feedDetailAdapter by lazy {
        FeedDetailAdapter(
            onEditClickListener = { onEditClickListener() },
            onDeleteClickListener = { onDeleteClickListener() },
            onEmotionClickListener = {},
            onReportClickListener = {},
            onUserClickListener = {},
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
            rvFeedDetail.adapter = feedDetailAdapter
            val testComment = mutableListOf<FeedDetail>()
            repeat(15) {
                testComment.add(
                    FeedDetail.BODY(
                        Comment(
                            commentId = 5906,
                            name = "Roscoe Bowman",
                            uploadDate = Date(),
                            lastEditDate = null,
                            content = "eruditi"
                        )
                    )
                )
            }
            feedDetailAdapter.submitList(testComment)
        }
    }

    private fun onEditClickListener() {

    }

    private fun onDeleteClickListener() {

    }
}