package com.data.model.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSingleTicket(
    @SerialName("type")
    val type: String,
    @SerialName("image")
    val image: String,
    @SerialName("seat")
    val seat: String,
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("name")
    val name: String,
    @SerialName("category")
    val category: String,
    @SerialName("location")
    val location: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("reviewExists")
    val reviewExists: Boolean,
    @SerialName("reviewId")
    val reviewId: Int
)