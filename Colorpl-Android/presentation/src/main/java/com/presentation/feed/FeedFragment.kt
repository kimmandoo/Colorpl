package com.presentation.feed

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.SimpleItemAnimator
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val viewModel: FeedViewModel by viewModels()
    private val filterAdapter by lazy {
        FilterAdapter(onItemClickListener = { filterItem ->
            onFilterClickListener(filterItem)
        })
    }
    private val loading by lazy {
        LoadingDialog(requireContext())
    }
    private val feedAdapter by lazy {
        FeedAdapter(
            onFeedContentClickListener = { id ->
                onFeedContentClickListener(id)
            },
            onEmotionClickListener = { id, isEmpathy ->
                onEmotionClickListener(id, isEmpathy)
            },
            onReportClickListener = { onReportClickListener() },
            onUserClickListener = { onUserClickListener() },
        )
    }

    override fun onResume() {
        super.onResume()
        feedAdapter.refresh()
        binding.rvFeed.scrollToPosition(0)
    }

    override fun initView() {
        initFilter()
        initFeed()
        onFeedRegisterClickListener()
        observeRefreshTrigger()
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

        viewModel.pagedFeed.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { pagingData ->
            pagingData?.let { feed ->
                feedAdapter.submitData(viewLifecycleOwner.lifecycle, feed)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        feedAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadStates ->
                val isLoading = loadStates.source.refresh is LoadState.Loading
                if (!isLoading) loading.dismiss() else loading.show()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeRefreshTrigger() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.refreshTrigger.collectLatest {
                feedAdapter.refresh()
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

    private fun onFeedContentClickListener(reviewId: Int) {
        val action = FeedFragmentDirections.actionFragmentFeedToFragmentFeedDetail(reviewId)
        navigateDestination(action)
    }

    private fun onEmotionClickListener(id: Int, isEmpathy: Boolean) {
        viewModel.toggleEmpathy(id, isEmpathy)
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