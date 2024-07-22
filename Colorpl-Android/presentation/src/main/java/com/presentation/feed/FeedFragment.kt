package com.presentation.feed

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedBinding
import com.domain.model.Feed
import com.domain.model.FilterItem
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FeedAdapter
import com.presentation.component.adapter.feed.FilterAdapter
import com.presentation.util.getFilterItems
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date


@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val filterAdapter by lazy {
        FilterAdapter(onItemClickListener = { filterItem ->
            onFilterClickListener(filterItem)
        })
    }
    private val feedAdapter by lazy {
        FeedAdapter(
            onFeedContentClickListener = { onFeedContentClickListener() },
            onCommentClickListener = { onCommentClickListener() },
            onEmotionClickListener = { onEmotionClickListener() },
            onReportClickListener = { onReportClickListener() },
            onUserClickListener = { onUserClickListener() }
        )
    }

    override fun initView() {
        binding.apply {
            initFilter()
            initFeed()
        }
    }

    private fun initFilter() {
        binding.rvFilter.apply {
            adapter = filterAdapter
            itemAnimator = null
        }
        filterAdapter.submitList(binding.root.context.getFilterItems())
    }

    private fun initFeed() {
        binding.rvFeed.apply {
            adapter = feedAdapter
            itemAnimator = null
        }
        val testFeed = mutableListOf<Feed>()
        repeat(10) {
            testFeed.add(
                Feed(
                    feedId = 7932,
                    title = "invenire",
                    userName = "Krista Crawford",
                    userProfileImg = null,
                    contentImg = null,
                    emotionMode = "discere",
                    emotionTotal = 4013,
                    commentTotal = 7193,
                    uploadedDate = Date()
                )
            )
        }
        feedAdapter.submitList(testFeed)
    }

    private fun onFilterClickListener(clickedItem: FilterItem) {
        val updatedList = filterAdapter.currentList.map { item ->
            if (item.name == clickedItem.name) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }

        filterAdapter.submitList(updatedList)
    }

    private fun onFeedContentClickListener() {

    }

    private fun onCommentClickListener() {

    }

    private fun onEmotionClickListener() {

    }

    private fun onReportClickListener() {

    }

    private fun onUserClickListener() {

    }
}