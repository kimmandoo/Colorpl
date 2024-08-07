package com.data.datasourceimpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.data.datasource.local.TokenDataSource
import com.data.model.request.RequestSignToken
import com.data.model.response.ResponseSignToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class TokenDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenDataSource {

    override suspend fun setSignToken(requestSignToken: RequestSignToken) {
        dataStore.edit { prefs ->
            prefs[EMAIL] = requestSignToken.email
            prefs[PASSWORD] = requestSignToken.password
            prefs[ACCESS_TOKEN_KEY] = requestSignToken.accessToken
            prefs[REFRESH_TOKEN_KEY] = requestSignToken.refreshToken
            prefs[ID_TOKEN] = requestSignToken.idToken
        }
    }

    override suspend fun getSignToken(): ResponseSignToken = dataStore.data.map { prefs ->
        ResponseSignToken(
            prefs[EMAIL] ?: "",
            prefs[PASSWORD] ?: "",
            prefs[ACCESS_TOKEN_KEY] ?: "",
            prefs[REFRESH_TOKEN_KEY] ?: "",
            prefs[ID_TOKEN] ?: ""
        )

    }.first()

    override suspend fun getAccessToken(): String = dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN_KEY] ?: ""
    }.first()

    override suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
        }
    }


    companion object {
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ID_TOKEN = stringPreferencesKey("id_token")
    }
}