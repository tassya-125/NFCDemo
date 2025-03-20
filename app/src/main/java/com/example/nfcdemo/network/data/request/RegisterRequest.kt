package com.example.nfcdemo.network.data.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("identifier")
    val identifier: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("isPhone")
    val isPhone: Boolean
)