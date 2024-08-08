package com.data.datasource.remote

interface PayDataSource {

    suspend fun getPaymentToken() : String
}