package com.presentation.feed

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedBinding
import com.domain.model.FilterItem
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FeedAdapter
import com.presentation.component.adapter.feed.FilterAdapter
import com.presentation.component.dialog.LoadingDialog
import com.presentation.util.getFilterItems
import com.presentation.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


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
        initFilter()
        initFeed()
        onFeedRegisterClickListener()
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
        val loading = LoadingDialog(requireContext())
        viewModel.pagedFeed.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { pagingData ->
            pagingData?.let { feed ->
                feedAdapter.submitData(feed)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        feedAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadStates ->
                val isLoading = loadStates.source.refresh is LoadState.Loading
                if (!isLoading) loading.dismiss() else loading.show()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
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

    private fun onFeedRegisterClickListener() {
        binding.imgFeedRegister.setOnClickListener {
            navigateDestination(R.id.action_fragment_feed_to_fragment_feed_ticket_select)
        }
    }
}