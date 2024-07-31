package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.component.custom.ListStateFlow
import com.presentation.util.Category
import com.presentation.util.Sign
import com.presentation.util.emailCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : ViewModel() {


    private val _typeEvent = MutableSharedFlow<Sign>()
    val typeEvent: SharedFlow<Sign>
        get() = _typeEvent

    fun setTypeEvent(type: Sign) {
        viewModelScope.launch {
            _typeEvent.emit(type)
        }
    }


    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> get() = _userEmail

    private val _userNickName = MutableStateFlow("")
    private val _userPassWord = MutableStateFlow("")
    private val _userImage = MutableStateFlow("")

    fun setUserEmail(value: String) {
        _userEmail.value = value
    }

    fun setUserNickName(value: String) {
        _userNickName.value = value
    }

    fun setPassWord(value: String) {
        _userPassWord.value = value
    }

    fun setUserImage(value: String) {
        _userImage.value = value
    }

    private val _nextButton = MutableSharedFlow<Boolean>(1)
    val nextButton: SharedFlow<Boolean> get() = _nextButton


    val userPreference = ListStateFlow<Category>()

    private val _completeButton = MutableSharedFlow<Boolean>(1)
    val completeButton: SharedFlow<Boolean> get() = _completeButton

    init {
        checkSignNext()
        checkCompleteNext()
    }

    private fun checkSignNext() {
        combine(
            _userEmail,
            _userNickName,
            _userImage,
            _userPassWord
        ) { email, nickname, image, password ->
            email.isNotEmpty() && email.emailCheck() && nickname.isNotEmpty() && image.isNotEmpty() && password.isNotEmpty()
        }.onEach { isEnabled ->
            _nextButton.emit(isEnabled)
        }.launchIn(viewModelScope)
    }

    private fun checkCompleteNext() {
        viewModelScope.launch {
            userPreference.items.collectLatest { item ->
                Timber.d("체크 아이템 확인 $item")
                _completeButton.emit(item.isNotEmpty())
            }
        }
    }


    fun clearData() {
        Timber.d("삭제가 왜 호출됨")
        setUserImage("")
        setUserEmail("")
        setUserNickName("")
        setPassWord("")
        userPreference.clear()
    }

}