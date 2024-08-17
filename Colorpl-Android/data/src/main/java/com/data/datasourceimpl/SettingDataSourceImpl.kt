package com.data.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.data.datasource.local.SettingDataSource
import com.data.model.response.ResponseSetting
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class SettingDataSourceImpl @Inject constructor(
    @Named("SETTING_DATASTORE") private val dataStore: DataStore<Preferences>
) : SettingDataSource {

    override suspend fun getSettingsInfo(): ResponseSetting = dataStore.data.map { prefs ->
        ResponseSetting(
            prefs[NOTIFICATION_SEND] ?: false,
            prefs[NOTIFICATION_VIBRATE] ?: false,
            prefs[NOTIFICATION_SOUND] ?: false,
        )
    }.first()

    override suspend fun setOnNotification() {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_SEND] = true
        }
    }

    override suspend fun setOnNotificationSound() {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_SOUND] = true
        }
    }

    override suspend fun setOnNotificationVibrate() {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_VIBRATE] = true
        }
    }

    override suspend fun setOffNotification() {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_SEND] = false
        }
    }

    override suspend fun setOffNotificationSound() {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_SOUND] = false
        }
    }

    override suspend fun setOffNotificationVibrate() {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_VIBRATE] = false
        }
    }

    companion object PreferencesKeys {
        val NOTIFICATION_SEND = booleanPreferencesKey("notification_send")
        val NOTIFICATION_VIBRATE = booleanPreferencesKey("notification_vibrate")
        val NOTIFICATION_SOUND = booleanPreferencesKey("notification_sound")
    }
}