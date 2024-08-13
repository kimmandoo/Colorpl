package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Setting
import com.domain.usecaseimpl.setting.SettingUseCase
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
    private val signOutUseCase: SingOutUseCase,
    private val settingUseCase: SettingUseCase
) : ViewModel() {
    private val _signOutEvent = MutableSharedFlow<Any?>()
    val signOutEvent: SharedFlow<Any?> = _signOutEvent.asSharedFlow()
    private val _settingsInfo = MutableSharedFlow<Setting>()
    val settingsInfo = _settingsInfo.asSharedFlow()

    init {
        getSettingInfo()
    }

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

    fun updateNotificationSend(isChecked: Boolean) {
        viewModelScope.launch {
            if (isChecked) {
                settingUseCase.setOnNotification()
            } else {
                settingUseCase.setOffNotification()
            }
            getSettingInfo()
        }
    }

    fun updateNotificationSound(isChecked: Boolean) {
        viewModelScope.launch {
            if (isChecked) {
                settingUseCase.setOnNotificationSound()
            } else {
                settingUseCase.setOffNotificationSound()
            }
            getSettingInfo()
        }
    }

    fun updateNotificationVibrate(isChecked: Boolean) {
        viewModelScope.launch {
            if (isChecked) {
                settingUseCase.setOnNotificationVibrate()
            } else {
                settingUseCase.setOffNotificationVibrate()
            }
            getSettingInfo()
        }
    }

    private fun getSettingInfo() {
        viewModelScope.launch {
            settingUseCase.getSettingsInfo().collect {
                _settingsInfo.emit(it)
            }
        }
    }

}