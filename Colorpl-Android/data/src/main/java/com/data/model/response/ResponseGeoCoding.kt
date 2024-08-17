package com.data.model.response


import kotlinx.serialization.SerialName

data class ResponseGeoCoding(
    @SerialName("status")
    val status: String,
    @SerialName("meta")
    val meta: Meta,
    @SerialName("addresses")
    val addresses: List<Addresses>,
    @SerialName("errorMessage")
    val errorMessage: String
) {
    data class Meta(
        @SerialName("totalCount")
        val totalCount: Int,
        @SerialName("page")
        val page: Int,
        @SerialName("count")
        val count: Int
    )
    data class Addresses(
        @SerialName("roadAddress")
        val roadAddress: String,
        @SerialName("jibunAddress")
        val jibunAddress: String,
        @SerialName("englishAddress")
        val englishAddress: String,
        @SerialName("addressElements")
        val addressElements: List<AddressElement>,
        @SerialName("x")
        val x: String,
        @SerialName("y")
        val y: String,
        @SerialName("distance")
        val distance: Double
    ) {
        data class AddressElement(
            @SerialName("types")
            val types: List<String>,
            @SerialName("longName")
            val longName: String,
            @SerialName("shortName")
            val shortName: String,
            @SerialName("code")
            val code: String
        )
    }
}