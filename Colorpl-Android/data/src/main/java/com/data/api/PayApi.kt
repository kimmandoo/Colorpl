package com.data.api

interface PayApi {

    suspend fun getPaymentToken() : String
}