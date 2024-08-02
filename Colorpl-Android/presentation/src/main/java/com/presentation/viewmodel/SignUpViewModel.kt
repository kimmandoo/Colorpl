package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.User
import com.domain.usecaseimpl.sign.SignUpUseCase
import com.domain.util.DomainResult
import com.presentation.component.custom.ListStateFlow
import com.presentation.sign.model.SignUpEventState
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
    private val signUpUseCase: SignUpUseCase
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
    val userNickName: StateFlow<String> get() = _userNickName

    private val _userPassWord = MutableStateFlow("")
    val userPassWord: StateFlow<String> get() = _userPassWord

    private val _userImage = MutableStateFlow("")
    val userImage: StateFlow<String> get() = _userImage

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

    private val _signUpUser = MutableStateFlow(User())
    val signUpUser: StateFlow<User> get() = _signUpUser

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

    private val _signUpEvent = MutableSharedFlow<SignUpEventState>()
    val signUpEvent: SharedFlow<SignUpEventState> get() = _signUpEvent


    fun signUp() { //회원 가입 서버 통신
        val user = User(
            email = userEmail.value,
            password = userPassWord.value,
            nickName = userNickName.value,
            profileImage = userImage.value
        )
        viewModelScope.launch {
            signUpUseCase.signUp(user).collectLatest {
                when (it) {
                    is DomainResult.Success -> {
                        _signUpEvent.emit(SignUpEventState.SignUpSuccess)
                    }

                    is DomainResult.Error -> {
                        Timber.d("회원가입 에러 확인 ${it.exception}")
                        _signUpEvent.emit(SignUpEventState.Error(it.exception.toString()))
                    }
                }
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