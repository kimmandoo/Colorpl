package com.presentation.feed

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedDetailBinding
import com.domain.model.Comment
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.CommentAdapter
import java.util.Date

class FeedDetailFragment : BaseFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail) {

    private val commentAdapter by lazy {
        CommentAdapter(
            onEditClickListener = { onEditClickListener() },
            onDeleteClickListener = { onDeleteClickListener() }
        )
    }

    override fun initView() {
        binding.apply {
            rvComment.adapter = commentAdapter


            val testComment = mutableListOf<Comment>()
            repeat(10) {
                testComment.add(
                    Comment(
                        commentId = 5906,
                        name = "Roscoe Bowman",
                        uploadDate = Date(),
                        lastEditDate = null,
                        content = "eruditi"
                    )
                )
            }
            commentAdapter.submitList(testComment)
        }
    }

    private fun onEditClickListener() {

    }

    private fun onDeleteClickListener() {

    }
}