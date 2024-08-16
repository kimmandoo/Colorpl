package com.data.repositoryimpl

import com.data.datasource.local.SettingDataSource
import com.data.model.response.ResponseSetting
import com.data.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDataSource: SettingDataSource
) : SettingRepository {
    override suspend fun getSettingsInfo(): Flow<ResponseSetting> = flow {
        emit(settingDataSource.getSettingsInfo())
    }

    override suspend fun setOnNotification() {
        return settingDataSource.setOnNotification()
    }

    override suspend fun setOnNotificationSound() {
        return settingDataSource.setOnNotificationSound()
    }

    override suspend fun setOnNotificationVibrate() {
        return settingDataSource.setOnNotificationVibrate()
    }

    override suspend fun setOffNotification() {
        return settingDataSource.setOffNotification()
    }

    override suspend fun setOffNotificationSound() {
        return settingDataSource.setOffNotificationSound()
    }

    override suspend fun setOffNotificationVibrate() {
        return settingDataSource.setOffNotificationVibrate()
    }
}