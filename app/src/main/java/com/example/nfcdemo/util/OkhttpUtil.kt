package com.example.nfcdemo.util

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

object OkhttpUtil  {

    // OkHttpClient 实例
    val okHttpClient: OkHttpClient

    // Base URL
    val baseUrl = "http://127.0.0.1:9000/"

    // 初始化代码
    init {
        okHttpClient = OkHttpClient.Builder().build()
    }

    // GET 方法
    fun get(endpoint: String, callback: Callback) {
        val url = baseUrl + endpoint
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    // POST 方法，接收 Map 数据并将其转换为 JSON 字符串
    fun post(endpoint: String, params: Map<String, Any>, callback: Callback) {
        val url = baseUrl + endpoint

        // 使用 Gson 将 Map 转换为 JSON 字符串
        val json = Gson().toJson(params)

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, json)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(callback)
    }

    // POST 方法，接收 Map 数据并将其转换为 JSON 字符串
    fun post(endpoint: String, params: Any, callback: Callback) {
        val url = baseUrl + endpoint

        // 使用 Gson 将 Map 转换为 JSON 字符串
        val json = Gson().toJson(params)

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, json)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(callback)
    }
}
