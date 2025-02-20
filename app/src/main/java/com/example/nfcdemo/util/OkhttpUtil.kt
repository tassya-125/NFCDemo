package com.example.nfcdemo.util

import com.google.gson.Gson
import okhttp3.*

class OkhttpUtil private constructor() {

    // OkHttpClient 实例
    val okHttpClient: OkHttpClient

    // Base URL
    val baseUrl = "http://127.0.0.1:9000/"

    // 初始化代码
    init {
        okHttpClient = OkHttpClient.Builder().build()
    }

    // 单例对象
    companion object {
        @Volatile
        private var instance: OkhttpUtil? = null

        fun getInstance(): OkhttpUtil {
            var tempInstance = instance
            if (tempInstance == null) {
                synchronized(this) {
                    tempInstance = instance
                    if (tempInstance == null) {
                        instance = OkhttpUtil()
                        tempInstance = instance
                    }
                }
            }
            return tempInstance!!
        }
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

        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val requestBody = RequestBody.create(mediaType, json)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(callback)
    }
}
