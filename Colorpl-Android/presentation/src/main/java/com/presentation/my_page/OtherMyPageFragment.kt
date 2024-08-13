package com.presentation.my_page

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentOtherMyPageBinding
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FeedAdapter
import com.presentation.component.dialog.LoadingDialog
import com.presentation.my_page.model.OtherMyPageEventState
import com.presentation.util.setImageCircleCrop
import com.presentation.util.setVisibility
import com.presentation.viewmodel.FeedViewModel
import com.presentation.viewmodel.MyPageViewModel
import com.presentation.viewmodel.OtherMyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class OtherMyPageFragment :
    BaseFragment<FragmentOtherMyPageBinding>(R.layout.fragment_other_my_page) {
    private val myPageViewModel: MyPageViewModel by viewModels()
    private val otherMyPageViewModel: OtherMyPageViewModel by viewModels()
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
        initData()
        initFeed()
        initClickEvent()
        observeUiState()
        observeRefreshTrigger()
        observeEventState()
    }


    private fun initData() {
        val safeArgs: OtherMyPageFragmentArgs by navArgs()
        safeArgs.memberInfo.apply {
            binding.isFollow = isFollowing
            binding.tvFollowRequest.isSelected = isFollowing
            otherMyPageViewModel.setFollowerId(memberId)
            myPageViewModel.getMemberInfo(true, memberId)
        }

    }

    private fun initFeed() {
        binding.rcFeed.apply {
            adapter = feedAdapter
            itemAnimator = null
        }
        val loading = LoadingDialog(requireContext())
        otherMyPageViewModel.otherFeedData.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { pagingData ->
                pagingData.let { feed ->
                    feedAdapter.submitData(viewLifecycleOwner.lifecycle, feed)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        feedAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadStates ->
                val isLoading = loadStates.source.refresh is LoadState.Loading
                if (!isLoading) {
                    binding.icEmptyView.clTitle.setVisibility(feedAdapter.itemCount == 0)
                    binding.rcFeed.setVisibility(feedAdapter.itemCount > 0)
                    loading.dismiss()
                } else loading.show()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeUiState() {
        myPageViewModel.memberUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.member = it.memberInfo
                binding.ivProfileImg.setImageCircleCrop(it.memberInfo?.profileImage, true)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEventState() {
        otherMyPageViewModel.otherMyPageEventState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                myPageViewModel.getMemberInfo(true, otherMyPageViewModel.followerId.value)
                when (it) {
                    is OtherMyPageEventState.Follow -> {
                        binding.isFollow = true
                        binding.tvFollowRequest.isSelected = true
                    }

                    is OtherMyPageEventState.UnFollow -> {
                        binding.isFollow = false
                        binding.tvFollowRequest.isSelected = false
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)


    }
    private fun observeRefreshTrigger() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.refreshTrigger.collectLatest {
                feedAdapter.refresh()
            }
        }
    }

    private fun initClickEvent() {
        binding.apply {
            clBack.setOnClickListener {
                navigatePopBackStack()
            }
            tvFollowRequest.setOnClickListener {
                if (!binding.tvFollowRequest.isSelected) {
                    otherMyPageViewModel.follow()
                } else {
                    otherMyPageViewModel.unFollow()
                }
            }
        }
    }

    private fun onFeedContentClickListener(reviewId: Int) {
        navigateDestinationBundle(
            R.id.action_fragment_other_my_page_to_fragment_feed_detail,
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
