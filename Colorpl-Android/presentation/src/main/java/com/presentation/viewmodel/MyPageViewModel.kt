package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Member
import com.domain.usecaseimpl.member.GetMemberInfoUseCase
import com.domain.util.DomainResult
import com.presentation.my_page.model.MemberUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMemberInfoUseCase: GetMemberInfoUseCase
) : ViewModel() {


    private val _memberUiState = MutableStateFlow(MemberUiState())
    val memberUiState: StateFlow<MemberUiState> get() = _memberUiState

    fun setMemberInfo(value: Member) {
        _memberUiState.update { member ->
            member.copy(
                memberInfo = value
            )
        }
    }

    init {
        getMemberInfo()
    }

    fun getMemberInfo() {
        viewModelScope.launch {
            getMemberInfoUseCase.getMemberInfo().collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        setMemberInfo(result.data)
                    }

                    is DomainResult.Error -> {
                        Timber.d("멤버 네트워크 오류 ${result.exception}")
                    }
                }
            }
        }
    }
}