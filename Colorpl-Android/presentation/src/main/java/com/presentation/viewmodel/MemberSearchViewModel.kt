package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.MemberSearch
import com.domain.usecaseimpl.member.GetMemberSearchUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MemberSearchViewModel @Inject constructor(
    private val getMemberSearchUseCase: GetMemberSearchUseCase
) : ViewModel() {

    private val _memberSearchInfo = MutableStateFlow<List<MemberSearch>>(emptyList())
    val memberSearchInfo: StateFlow<List<MemberSearch>> get() = _memberSearchInfo

    fun setMemberSearchInfo(value: List<MemberSearch>) {
        _memberSearchInfo.value = value
    }


    fun getMemberSearchInfo(nickname: String) {
        viewModelScope.launch {
            getMemberSearchUseCase.getMemberSearchInfo(nickname).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        setMemberSearchInfo(result.data)
                    }
                    is DomainResult.Error -> {
                        Timber.d("유저 검색 실패 ${result.exception}")
                    }
                }
            }
        }
    }
}