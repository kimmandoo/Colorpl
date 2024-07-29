package com.presentation.feed

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedBinding
import com.domain.model.FilterItem
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FeedAdapter
import com.presentation.component.adapter.feed.FilterAdapter
import com.presentation.util.getFilterItems
import com.presentation.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val viewModel: FeedViewModel by viewModels()
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
        Timber.d("${viewModel.pagedFeed}")
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagedFeed.collectLatest { pagingData ->
                Timber.d("pagedFeed call")
                feedAdapter.submitData(pagingData)
            }
        }
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
        findNavController().navigate(R.id.action_fragment_feed_to_fragment_feed_detail)
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