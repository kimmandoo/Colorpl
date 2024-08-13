package com.domain.usecaseimpl.setting

import com.data.repository.SettingRepository
import com.domain.mapper.toEntity
import com.domain.model.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {
    suspend fun getSettingsInfo(): Flow<Setting> = flow {
        settingRepository.getSettingsInfo().collect {
            emit(it.toEntity())
        }
    }

    suspend fun setOnNotification() {
        return settingRepository.setOnNotification()
    }

    suspend fun setOnNotificationSound() {
        return settingRepository.setOnNotificationSound()
    }

    suspend fun setOnNotificationVibrate() {
        return settingRepository.setOnNotificationVibrate()
    }

    suspend fun setOffNotification() {
        return settingRepository.setOffNotification()
    }

    suspend fun setOffNotificationSound() {
        return settingRepository.setOffNotificationSound()
    }

    suspend fun setOffNotificationVibrate() {
        return settingRepository.setOffNotificationVibrate()
    }
}