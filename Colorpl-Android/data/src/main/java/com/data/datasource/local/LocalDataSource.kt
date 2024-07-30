package com.data.datasource.local

interface LocalDataSource {

    suspend fun getAccessToken() : String
    suspend fun setAccessToken(accessToken : String)
}