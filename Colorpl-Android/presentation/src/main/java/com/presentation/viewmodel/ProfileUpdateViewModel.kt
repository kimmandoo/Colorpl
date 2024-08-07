package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Member
import com.domain.usecaseimpl.member.UpdateMemberInfoUseCase
import com.domain.util.DomainResult
import com.presentation.my_page.model.MemberUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileUpdateViewModel @Inject constructor(
    private val updateMemberInfoUseCase: UpdateMemberInfoUseCase
) : ViewModel() {


    private val _updateNickName = MutableStateFlow("")

    fun setUpdateNickName(value: String) {
        _updateNickName.value = value
    }

    private val _profileImageFile = MutableStateFlow<File?>(null)

    fun setUpdateProfileImageFile(value: File?) {
        _profileImageFile.value = value
    }

    private val _updateSuccess = MutableSharedFlow<Boolean>()
    val updateSuccess : SharedFlow<Boolean> get() = _updateSuccess

    fun updateMemberInfo() {
        viewModelScope.launch {
            updateMemberInfoUseCase.updateMemberInfo(
                _updateNickName.value,
                _profileImageFile.value
            ).collectLatest { result ->
                when(result){
                    is DomainResult.Success -> {
                        _updateSuccess.emit(true)
                    }
                    is DomainResult.Error -> {
                        _updateSuccess.emit(false)
                    }
                }
            }
        }
    }

}