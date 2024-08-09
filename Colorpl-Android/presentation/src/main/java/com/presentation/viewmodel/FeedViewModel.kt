package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.domain.model.Comment
import com.domain.model.Feed
import com.domain.model.Review
import com.domain.model.ReviewDetail
import com.domain.usecase.CommentUseCase
import com.domain.usecase.FeedUseCase
import com.domain.usecase.ReviewDeleteUseCase
import com.domain.usecase.ReviewEditUseCase
import com.domain.usecase.ReviewEmpathyUseCase
import com.domain.usecaseimpl.review.GetReviewDetailUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPagedFeedUseCase: FeedUseCase,
    private val commentUseCase: CommentUseCase,
    private val getReviewDetailUseCase: GetReviewDetailUseCase,
    private val reviewEditUseCase: ReviewEditUseCase,
    private val reviewDeleteUseCase: ReviewDeleteUseCase,
    private val reviewEmpathyUseCase: ReviewEmpathyUseCase
) : ViewModel() {
    private val _pagedFeed = MutableStateFlow<PagingData<Feed>?>(null)
    val pagedFeed = _pagedFeed
    private val _pagedComment = MutableStateFlow<PagingData<Comment>?>(null)
    val pagedComment = _pagedComment
    private val _reviewEditResponse = MutableSharedFlow<Int>()
    val reviewEditResponse = _reviewEditResponse.asSharedFlow()
    private val _reviewDeleteResponse = MutableSharedFlow<Int>()
    val reviewDeleteResponse = _reviewDeleteResponse.asSharedFlow()
    private val _commentEditResponse = MutableSharedFlow<Int>()
    val commentEditResponse = _commentEditResponse.asSharedFlow()
    private val _commentDeleteResponse = MutableSharedFlow<Int>()
    val commentDeleteResponse = _commentDeleteResponse.asSharedFlow()
    private val _commentCreateResponse = MutableSharedFlow<Int>()
    val commentCreateResponse = _commentCreateResponse.asSharedFlow()
    private val _refreshTrigger = MutableSharedFlow<Unit>()
    val refreshTrigger = _refreshTrigger.asSharedFlow()

    //리뷰 상세
    private val _reviewDetail = MutableStateFlow(ReviewDetail())
    val reviewDetail: StateFlow<ReviewDetail> get() = _reviewDetail

    init {
        getFeed()
    }

    fun toggleEmpathy(reviewId: Int, isEmpathy: Boolean) {
        viewModelScope.launch {
            val result = if (isEmpathy) {
                reviewEmpathyUseCase.removeEmpathy(reviewId)
            } else {
                reviewEmpathyUseCase.addEmpathy(reviewId)
            }

            result.collect {
                when (it) {
                    is DomainResult.Error -> {
                        Timber.tag("empathy").d("${it.exception}")
                    }

                    is DomainResult.Success -> {
                        _refreshTrigger.emit(Unit)
                    }
                }
            }
        }
    }

    fun deleteReview(reviewId: Int) {
        viewModelScope.launch {
            reviewDeleteUseCase(reviewId).collect {
                when (it) {
                    is DomainResult.Success -> {
                        _reviewDeleteResponse.emit(it.data)
                    }

                    is DomainResult.Error -> {
                        _reviewDeleteResponse.emit(-1)
                        Timber.d("review delete에러 ${it.exception}")
                    }
                }
            }
        }
    }

    fun editReview(reviewId: Int, review: Review) {
        viewModelScope.launch {
            reviewEditUseCase(reviewId, requestReviewEdit = review).collect {
                when (it) {
                    is DomainResult.Success -> {
                        _reviewEditResponse.emit(it.data)
                        getReviewDetail(reviewId)
                    }

                    is DomainResult.Error -> {
                        _reviewEditResponse.emit(-1)
                        Timber.d("review edit 에러 ${it.exception}")
                    }
                }
            }
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            commentUseCase.deleteComment(commentId).collect {
                when (it) {
                    is DomainResult.Success -> {
                        _commentDeleteResponse.emit(it.data)
                    }

                    is DomainResult.Error -> {
                        _commentDeleteResponse.emit(-1)
                        Timber.d("comment delete에러 ${it.exception}")
                    }
                }
            }
        }
    }

    fun createComment(reviewId: Int, commentContent: String) {
        viewModelScope.launch {
            commentUseCase.createComment(reviewId, commentContent).collect {
                when (it) {
                    is DomainResult.Success -> {
                        _commentCreateResponse.emit(it.data)
                    }

                    is DomainResult.Error -> {
                        _commentCreateResponse.emit(-1)
                        Timber.d("comment cretate 에러 ${it.exception}")
                    }
                }
            }
        }
    }

    fun editComment(reviewId: Int, commentId: Int, commentContent: String) {
        viewModelScope.launch {
            commentUseCase.editComment(
                reviewId = reviewId,
                commentId = commentId,
                commentContent = commentContent
            ).collect {
                when (it) {
                    is DomainResult.Success -> {
                        _commentEditResponse.emit(it.data)
                    }

                    is DomainResult.Error -> {
                        _commentEditResponse.emit(-1)
                        Timber.d("comment edit에러 ${it.exception}")
                    }
                }
            }
        }
    }

    fun getFeed() {
        viewModelScope.launch {
            getPagedFeedUseCase().cachedIn(viewModelScope).collectLatest { pagedData ->
                _pagedFeed.value = pagedData
            }
        }
    }

    fun getComment(reviewId: Int) {
        viewModelScope.launch {
            commentUseCase.getComment(reviewId).cachedIn(viewModelScope)
                .collectLatest { pagedData ->
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