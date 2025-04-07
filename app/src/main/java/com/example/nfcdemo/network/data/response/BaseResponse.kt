package com.example.nfcdemo.network.data.response

data class BaseResponse<T>(
    val code: Int,            // 响应码
    val msg: String?,         // 响应消息
    val data: T?              // 响应数据
)