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
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FeedDetailFragment :
    BaseDialogFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail) {

    private val viewModel: FeedViewModel by viewModels()

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
        binding.apply {
            val loading = LoadingDialog(requireContext())
            viewModel.getComment(feedId = 1)
            rvComment.adapter = commentAdapter
            viewModel.pagedComment.flowWithLifecycle(viewLifecycleOwner.lifecycle)
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

    private fun onEditClickListener() {

    }

    private fun onDeleteClickListener() {

    }
}