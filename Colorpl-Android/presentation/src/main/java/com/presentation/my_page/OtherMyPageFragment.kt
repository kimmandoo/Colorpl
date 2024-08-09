package com.presentation.my_page

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
import com.presentation.viewmodel.MyPageViewModel
import com.presentation.viewmodel.OtherMyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class OtherMyPageFragment :
    BaseFragment<FragmentOtherMyPageBinding>(R.layout.fragment_other_my_page) {
    private val myPageViewModel: MyPageViewModel by viewModels()
    private val otherMyPageViewModel: OtherMyPageViewModel by viewModels()
    private val feedAdapter by lazy {
        FeedAdapter(
            onFeedContentClickListener = { id ->

            },
            onCommentClickListener = { },
            onEmotionClickListener = { },
            onReportClickListener = { },
            onUserClickListener = { }
        )
    }

    override fun initView() {
        initData()
        initFeed()
        initClickEvent()
        observeUiState()
        observeEventState()
    }


    private fun initData() {
        val safeArgs: OtherMyPageFragmentArgs by navArgs()
        safeArgs.memberInfo.apply {
            Timber.d("데이ㅓㅌ 확인 $this")
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
                    feedAdapter.submitData(feed)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        feedAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadStates ->
                val isLoading = loadStates.source.refresh is LoadState.Loading
                if (!isLoading) loading.dismiss() else loading.show()
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

    private fun initClickEvent() {
        binding.apply {
            ivBack.setOnClickListener {
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
}
