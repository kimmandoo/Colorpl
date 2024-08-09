package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.domain.model.Feed
import com.domain.usecase.FeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReviewViewModel @Inject constructor(
    private val feedUseCase: FeedUseCase
) : ViewModel() {


    private val _pagedFeed = MutableStateFlow<PagingData<Feed>?>(null)
    val pagedFeed: StateFlow<PagingData<Feed>?> get() = _pagedFeed

    fun getFeed() {
        viewModelScope.launch {
            feedUseCase.getPagedMyFeed().cachedIn(viewModelScope)
                .collectLatest { pagedData ->
                    _pagedFeed.value = pagedData
                }
        }
    }

    init {
        getFeed()
    }
}