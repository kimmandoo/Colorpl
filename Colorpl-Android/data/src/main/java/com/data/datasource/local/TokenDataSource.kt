package com.data.datasource.local

interface TokenDataSource {

    suspend fun getAccessToken() : String
    suspend fun setAccessToken(accessToken : String)
}