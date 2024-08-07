package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.domain.model.Comment
import com.domain.model.Feed
import com.domain.model.ReviewDetail
import com.domain.usecase.CommentUseCase
import com.domain.usecase.FeedUseCase
import com.domain.usecaseimpl.review.GetReviewDetailUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPagedFeedUseCase: FeedUseCase,
    private val getPagedCommentUseCase: CommentUseCase,
    private val getReviewDetailUseCase: GetReviewDetailUseCase
) : ViewModel() {
    private val _pagedFeed = MutableStateFlow<PagingData<Feed>?>(null)
    val pagedFeed = _pagedFeed
    private val _pagedComment = MutableStateFlow<PagingData<Comment>?>(null)
    val pagedComment = _pagedComment


    //리뷰 상세
    private val _reviewDetail = MutableStateFlow(ReviewDetail())
    val reviewDetail: StateFlow<ReviewDetail> get() = _reviewDetail

    init {
        getFeed()
    }

    fun getFeed() {
        viewModelScope.launch {
            getPagedFeedUseCase().cachedIn(viewModelScope).collectLatest { pagedData ->
                _pagedFeed.value = pagedData
            }
        }
    }

    fun getComment(feedId: Int) {
        viewModelScope.launch {
            getPagedCommentUseCase(feedId).cachedIn(viewModelScope).collectLatest { pagedData ->
                _pagedComment.value = pagedData
            }
        }
    }

    fun getReviewDetail(reviewId: Int) {
        viewModelScope.launch {
            getReviewDetailUseCase.getReviewDetail(reviewId).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _reviewDetail.value = result.data
                    }

                    is DomainResult.Error -> {
                        Timber.d("리뷰 상세 데이터 에러 ${result.exception}")
                    }
                }
            }
        }
    }
}