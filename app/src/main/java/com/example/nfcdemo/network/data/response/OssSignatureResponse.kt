package com.example.nfcdemo.network.data.response

data class OssSignatureResponse(
    val accessKeyId: String,
    val policy: String,
    val signature: String,
    val host: String,
    val dir: String
)
