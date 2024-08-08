package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecaseimpl.sign.SingOutUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val signOutUseCase: SingOutUseCase
) : ViewModel() {
    private val _signOutEvent = MutableSharedFlow<Any?>()
    val signOutEvent: SharedFlow<Any?> = _signOutEvent.asSharedFlow()

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase.signOut().collect() { it ->
                when (it) {
                    is DomainResult.Error -> {
                        Timber.tag("signout").d("뿡뿡")
                        _signOutEvent.emit(-1)
                    }

                    is DomainResult.Success -> {
                        Timber.tag("signout").d("$it")
                        _signOutEvent.emit(1)
                    }
                }
            }
        }
    }
}