package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.domain.model.Feed
import com.domain.usecase.FeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPagedFeedUseCase: FeedUseCase
) : ViewModel() {
    private val _pagedFeed = MutableStateFlow<PagingData<Feed>?>(null)
    val pagedFeed = _pagedFeed

    fun getFeed(){
        viewModelScope.launch {
            getPagedFeedUseCase().cachedIn(viewModelScope).collectLatest { pagedData ->
                _pagedFeed.value = pagedData
            }
        }
    }
}