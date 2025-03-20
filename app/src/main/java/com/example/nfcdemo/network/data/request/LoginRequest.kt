package com.example.nfcdemo.network.data.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("username")
    val identifier: String,

    @SerializedName("password")
    val password: String
)