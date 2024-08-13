package com.presentation.my_page

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMyReviewBinding
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FeedAdapter
import com.presentation.component.dialog.LoadingDialog
import com.presentation.viewmodel.FeedViewModel
import com.presentation.viewmodel.MyReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyReviewFragment : BaseFragment<FragmentMyReviewBinding>(R.layout.fragment_my_review) {

    private val myReviewViewModel: MyReviewViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()

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


    override fun initView() {
        initFeed()
        initClickEvent()
        observeRefreshTrigger()
    }

    private fun initFeed() {
        binding.rcFeed.apply {
            adapter = feedAdapter
            itemAnimator = null
        }
        val loading = LoadingDialog(requireContext())
        myReviewViewModel.pagedFeed.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { pagingData ->
                pagingData?.let { feed ->
                    feedAdapter.submitData(feed)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        feedAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadStates ->
                val isLoading = loadStates.source.refresh is LoadState.Loading
                if (!isLoading) {
                    binding.count = feedAdapter.itemCount
                    loading.dismiss()
                } else loading.show()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initClickEvent() {
        binding.apply {
            includeTop.clBack.setOnClickListener {
                navigatePopBackStack()
            }

            imgFeedRegister.setOnClickListener {
                navigateDestination(R.id.action_fragment_my_review_to_fragment_feed_ticket_select)
            }
        }
    }

    private fun observeRefreshTrigger() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.refreshTrigger.collectLatest {
                feedAdapter.refresh()
            }
        }
    }

    private fun onFeedContentClickListener(reviewId: Int) {
        navigateDestinationBundle(
            R.id.action_fragment_my_review_to_fragment_feed_detail,
            bundleOf("reviewId" to reviewId)
        )
    }

    private fun onEmotionClickListener(id: Int, isEmpathy: Boolean) {
        feedViewModel.toggleEmpathy(id, isEmpathy)
    }

    private fun onReportClickListener() {

    }

    private fun onUserClickListener() {

    }
}