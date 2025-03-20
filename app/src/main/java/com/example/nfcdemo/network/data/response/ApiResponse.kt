package com.example.nfcdemo.network.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any?
)