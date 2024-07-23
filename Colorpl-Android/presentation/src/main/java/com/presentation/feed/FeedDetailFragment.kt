package com.presentation.feed

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

    override fun initView() {
        binding.apply {
            rvFeedDetail.adapter = feedDetailAdapter
            val testComment = mutableListOf<FeedDetail>()
            testComment.add(
                FeedDetail.HEADER(
                    Feed(
                        feedId = 1138,
                        title = "sed",
                        userName = "George Foster",
                        userProfileImg = null,
                        contentImg = null,
                        emotionMode = "definitionem",
                        emotionTotal = 1729,
                        commentTotal = 8645,
                        uploadedDate = Date()
                    )
                )
            )
            repeat(10) {
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