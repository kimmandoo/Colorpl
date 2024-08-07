package com.presentation.feed

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedDetailBinding
import com.presentation.base.BaseDialogFragment
import com.presentation.component.adapter.feed.CommentAdapter
import com.presentation.component.dialog.LoadingDialog
import com.presentation.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class FeedDetailFragment :
    BaseDialogFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail) {

    private val feedViewModel: FeedViewModel by viewModels()

    private val commentAdapter by lazy {
        CommentAdapter(
            onEditClickListener = { onEditClickListener() },
            onDeleteClickListener = { onDeleteClickListener() },
            onEmotionClickListener = {},
            onReportClickListener = {},
            onUserClickListener = {},
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        initData()
        initAdapter()
        observeReviewDetail()
    }

    private fun initData() {

        feedViewModel.getComment(feedId = 1)
        feedViewModel.getReviewDetail(1)
    }

    private fun initAdapter() {
        val loading = LoadingDialog(requireContext())
        binding.apply {
            rvComment.adapter = commentAdapter
            feedViewModel.pagedComment.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach { pagingData ->
                    pagingData?.let { comment ->
                        commentAdapter.submitData(comment)
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)

            commentAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .onEach { loadStates ->
                    val isLoading = loadStates.source.refresh is LoadState.Loading
                    if (!isLoading) loading.dismiss() else loading.show()
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun observeReviewDetail() {
        feedViewModel.reviewDetail.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                Timber.d("리뷰 상세 데이터 확인 $it")
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onEditClickListener() {

    }

    private fun onDeleteClickListener() {

    }
}