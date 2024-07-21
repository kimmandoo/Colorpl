package com.presentation.feed

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedBinding
import com.domain.model.FilterItem
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FilterAdapter
import com.presentation.util.getFilterItems
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private val filterAdapter by lazy {
        FilterAdapter(onItemClick = { filterItem ->
            onFilterClicked(filterItem)
        })
    }

    override fun initView() {
        binding.apply {
            initFilter()
        }
    }

    private fun initFilter() {
        binding.rvFilter.apply {
            adapter = filterAdapter
            itemAnimator = null
        }
        filterAdapter.submitList(binding.root.context.getFilterItems())
    }

    private fun onFilterClicked(clickedItem: FilterItem) {
        val updatedList = filterAdapter.currentList.map { item ->
            if (item.name == clickedItem.name) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }

        filterAdapter.submitList(updatedList)
    }
}