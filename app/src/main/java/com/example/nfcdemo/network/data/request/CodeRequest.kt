package com.example.nfcdemo.network.data.request

import com.google.gson.annotations.SerializedName

data class CodeRequest (
    @SerializedName("identifier")
    val password: String,
)