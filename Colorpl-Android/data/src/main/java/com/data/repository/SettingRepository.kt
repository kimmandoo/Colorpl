package com.data.repository

import com.data.model.request.RequestSignToken
import com.data.model.response.ResponseSetting
import com.data.model.response.ResponseSignToken
import com.data.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun getSettingsInfo(): Flow<ResponseSetting>
    suspend fun setOnNotification()
    suspend fun setOnNotificationSound()
    suspend fun setOnNotificationVibrate()
    suspend fun setOffNotification()
    suspend fun setOffNotificationSound()
    suspend fun setOffNotificationVibrate()
}