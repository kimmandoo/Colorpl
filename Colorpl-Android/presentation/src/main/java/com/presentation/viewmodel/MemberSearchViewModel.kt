package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.MemberSearch
import com.domain.usecaseimpl.member.GetMemberSearchUseCase
import com.domain.util.DomainResult
import com.presentation.my_page.model.MyPageEventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MemberSearchViewModel @Inject constructor(
    private val getMemberSearchUseCase: GetMemberSearchUseCase
) : ViewModel() {


    private val _memberSearchEvent = MutableSharedFlow<MyPageEventState>()
    val memberSearchEvent : SharedFlow<MyPageEventState> get() = _memberSearchEvent

    fun setMemberSearchEvent(value : MyPageEventState){
        viewModelScope.launch{
            _memberSearchEvent.emit(value)
        }
    }

    fun getMemberSearchInfo(nickname: String) {
        viewModelScope.launch {
            getMemberSearchUseCase.getMemberSearchInfo(nickname).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        setMemberSearchEvent(MyPageEventState.MemberSearchSuccess(result.data))
                    }
                    is DomainResult.Error -> {
                        setMemberSearchEvent(MyPageEventState.MemberSearchError)
                        Timber.d("유저 검색 실패 ${result.exception}")
                    }
                }
            }
        }
    }
}