package com.example.nfcdemo.network.data.response

import com.example.nfcdemo.model.User
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)