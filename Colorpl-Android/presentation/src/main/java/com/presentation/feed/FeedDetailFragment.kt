package com.presentation.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedDetailBinding
import com.domain.model.Comment
import com.domain.model.Review
import com.domain.model.ReviewDetail
import com.presentation.base.BaseDialogFragment
import com.presentation.component.adapter.feed.CommentAdapter
import com.presentation.component.dialog.ReviewEditDialog
import com.presentation.util.hideKeyboard
import com.presentation.util.setEmotion
import com.presentation.util.setImageCenterCrop
import com.presentation.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FeedDetailFragment :
    BaseDialogFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail) {

    private val feedViewModel: FeedViewModel by activityViewModels()
    private val args: FeedDetailFragmentArgs by navArgs()
    private val editDialog: ReviewEditDialog by lazy {
        ReviewEditDialog(requireContext(), binding.tvContent.text.toString()) {
            feedViewModel.editReview(
                feedViewModel.reviewDetail.value.id,
                Review(
                    feedViewModel.reviewDetail.value.id,
                    it,
                    feedViewModel.reviewDetail.value.spoiler,
                    feedViewModel.reviewDetail.value.emotion
                )
            )
        }
    }

    private lateinit var commentDialog: ReviewEditDialog

    private val commentAdapter by lazy {
        CommentAdapter(
            onEditClickListener = { comment ->
                onCommentEditClickListener(comment)
            },
            onDeleteClickListener = { id -> onCommentDeleteClickListener(id) },
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        initData()
        initEdit()
        initAdapter()
        observeReviewDetail()
        observeViewModel()
        observeComment()
        observeRefreshTrigger()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                feedViewModel.reviewDeleteResponse.collectLatest { reviewId ->
                    if (reviewId > 0) {
                        feedViewModel.getFeed()
                        dismiss()
                    }
                }
            }
            launch {
                feedViewModel.reviewEditResponse.collectLatest { reviewId ->
                    if (reviewId > 0) {
                        dismissLoading()
                        editDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun initData() {
        feedViewModel.getComment(args.reviewId)
        feedViewModel.getReviewDetail(args.reviewId)
    }

    private fun initEdit() {
        binding.apply {
            ivSendComment.setOnClickListener {
                if (etComment.text.toString().isNotEmpty()) {
                    binding.root.context.hideKeyboard(requireView())
                    feedViewModel.createComment(
                        feedViewModel.reviewDetail.value.id,
                        etComment.text.toString()
                    )
                    etComment.text.clear()
                }
            }
            tvEdit.setOnClickListener {
                editDialog.show()
            }
            tvDelete.setOnClickListener {
                feedViewModel.deleteReview(feedViewModel.reviewDetail.value.id)
            }
            tvProfile.setOnClickListener {
                val action = FeedDetailFragmentDirections.actionFragmentFeedDetailToFragmentUserSearch(feedViewModel.reviewDetail.value.writer)
                findNavController().navigate(action)
            }
        }
    }

    private fun initAdapter() {
        binding.apply {
            rvComment.adapter = commentAdapter
            rvComment.itemAnimator = null
        }

        feedViewModel.pagedComment.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { pagingData ->
                pagingData?.let { comment ->
                    commentAdapter.submitData(comment)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        commentAdapter.loadStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { loadStates ->
                val isLoading = loadStates.source.refresh is LoadState.Loading
                if (!isLoading) dismissLoading() else showLoading()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeReviewDetail() {
        feedViewModel.reviewDetail.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { data ->
                Timber.d("리뷰 상세 데이터 $data")
                feedViewModel.getReviewDetail(args.reviewId)
                feedViewModel.getFeed()
                updateReview(data)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeRefreshTrigger() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.refreshTrigger.collectLatest {
                feedViewModel.getReviewDetail(args.reviewId)
                feedViewModel.getFeed()
            }
        }
    }

    private fun observeComment() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                feedViewModel.commentDeleteResponse.collectLatest {
                    refresh()
                    dismissLoading()
                    commentAdapter.refresh()
                }
            }
            launch {
                feedViewModel.commentEditResponse.collectLatest {
                    refresh()
                    binding.root.context.hideKeyboard(requireView())
                    commentDialog.dismiss()
                    dismissLoading()
                    commentAdapter.refresh()
                }
            }
            launch {
                feedViewModel.commentCreateResponse.collectLatest {
                    refresh()
                    binding.etComment.clearFocus()
                    dismissLoading()
                    commentAdapter.refresh()
                }
            }
        }
    }

    private fun updateReview(data: ReviewDetail) {
        binding.apply {
            val ownerOptions = listOf(tvEdit, tvDelete)
            tvTitle.text = data.title
            tvContent.text = data.content
            ivContent.setImageCenterCrop(data.imgUrl)
            tvEmotion.text = data.empathy.toString()
            tvCommentCnt.text = data.commentCount.toString()
            ivEmotion.setEmotion(data.emotion)
            ivEmotion.setOnClickListener {
                onEmotionClickListener(data.id, data.myEmpathy)
            }
            ownerOptions.forEach { option ->
                option.visibility = if (!data.myReview) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
            ivEmotion.isSelected = data.myEmpathy
            tvProfile.text = data.writer
            tvUploadDate.text = data.createDate
        }
    }

    private fun onEmotionClickListener(id: Int, isEmpathy: Boolean) {
        feedViewModel.toggleEmpathy(id, isEmpathy)
    }

    private fun onCommentEditClickListener(comment: Comment) {
        commentDialog =
            ReviewEditDialog(requireContext(), comment.commentContent) { editedContent ->
                feedViewModel.editComment(
                    reviewId = comment.reviewId,
                    commentId = comment.id,
                    commentContent = editedContent
                )
                showLoading()
            }
        commentDialog.show()
    }

    private fun refresh(){
        feedViewModel.getComment(args.reviewId)
        feedViewModel.getReviewDetail(args.reviewId)
        feedViewModel.getFeed()
    }

    private fun onCommentDeleteClickListener(id: Int) {
        showLoading()
        feedViewModel.deleteComment(id)
    }
}