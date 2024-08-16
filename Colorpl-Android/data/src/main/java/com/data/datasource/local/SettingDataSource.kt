package com.data.datasource.local

import com.data.model.response.ResponseSetting

interface SettingDataSource {
    suspend fun getSettingsInfo(): ResponseSetting
    suspend fun setOnNotification()
    suspend fun setOnNotificationSound()
    suspend fun setOnNotificationVibrate()
    suspend fun setOffNotification()
    suspend fun setOffNotificationSound()
    suspend fun setOffNotificationVibrate()
}