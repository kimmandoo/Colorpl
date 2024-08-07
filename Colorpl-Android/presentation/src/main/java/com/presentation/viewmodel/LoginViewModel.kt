package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Member
import com.domain.model.SignToken
import com.domain.usecaseimpl.sign.SignInUseCase
import com.domain.util.DomainResult
import com.presentation.sign.model.SignInEventState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {


    private val _signInEvent = MutableSharedFlow<SignInEventState>()
    val signInEvent: SharedFlow<SignInEventState> get() = _signInEvent

    init{
        autoLogin()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase.signIn(Member(email, password)).collectLatest {
                when (it) {
                    is DomainResult.Success -> {
                        _signInEvent.emit(SignInEventState.SignInSuccess)
                    }

                    is DomainResult.Error -> {
                        Timber.d("로그인 에러 확인 ${it.exception}")
                        _signInEvent.emit(SignInEventState.Error(it.exception.toString()))
                    }
                }
            }
        }
    }

    fun autoLogin() {
        viewModelScope.launch {
            signInUseCase.getSignToken().collectLatest {
                when (it) {
                    is DomainResult.Success -> {
                        val data = it.data
                        if (data.email.isNotEmpty() && data.password.isNotEmpty()) {
                            signIn(data.email.toString(), data.password.toString())
                        }
                    }

                    is DomainResult.Error -> {
                        _signInEvent.emit(SignInEventState.Error(it.exception.toString()))
                    }
                }

            }
        }
    }

    fun googleSignIn(idToken: String) {
        viewModelScope.launch {
            signInUseCase.googleSignIn(idToken).collectLatest {
                when (it) {
                    is DomainResult.Success -> {
                        _signInEvent.emit(SignInEventState.SignInSuccess)
                    }

                    is DomainResult.Error -> {
                        Timber.d("구글 로그인 에러 확인 ${it.exception}")
                        _signInEvent.emit(SignInEventState.Error(it.exception.toString()))
                    }
                }
            }
        }
    }

}