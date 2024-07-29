package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.domain.model.Feed
import com.domain.usecase.FeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPagedFeedUseCase: FeedUseCase
) : ViewModel() {
    private val _pagedFeed = getPagedFeedUseCase().cachedIn(viewModelScope)
    val pagedFeed = _pagedFeed

}