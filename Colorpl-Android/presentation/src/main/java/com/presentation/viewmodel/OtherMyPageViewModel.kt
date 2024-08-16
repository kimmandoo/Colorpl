package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.domain.usecase.FeedUserUseCase
import com.domain.usecaseimpl.member.RequestFollowUnFollowUseCase
import com.domain.util.DomainResult
import com.presentation.my_page.model.OtherMyPageEventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OtherMyPageViewModel @Inject constructor(
    private val requestFollowUnFollowUseCase: RequestFollowUnFollowUseCase,
    private val feedUserUseCase: FeedUserUseCase
) : ViewModel() {



    private val _followerId = MutableStateFlow(0)
    val followerId: StateFlow<Int> get() = _followerId

    fun setFollowerId(value: Int) {
        _followerId.value = value
    }

    val otherFeedData = followerId.flatMapLatest { id ->
        feedUserUseCase(id)
    }.cachedIn(viewModelScope)

    private val _otherMyPageEventState = MutableSharedFlow<OtherMyPageEventState>()
    val otherMyPageEventState: SharedFlow<OtherMyPageEventState> get() = _otherMyPageEventState

    fun follow() {
        viewModelScope.launch {
            requestFollowUnFollowUseCase.follow(followerId.value).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _otherMyPageEventState.emit(OtherMyPageEventState.Follow)
                    }

                    is DomainResult.Error -> {
                        Timber.d("팔로우 실패 ${result.exception}")
                    }
                }
            }
        }
    }

    fun unFollow() {
        viewModelScope.launch {
            requestFollowUnFollowUseCase.unFollow(followerId.value).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _otherMyPageEventState.emit(OtherMyPageEventState.UnFollow)
                    }

                    is DomainResult.Error -> {
                        Timber.d("언팔로우 실패 ${result.exception}")
                    }
                }
            }
        }
    }
}