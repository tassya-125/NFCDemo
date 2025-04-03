package com.example.nfcdemo.util

import okhttp3.ResponseBody

object NetworkUtil {

    fun parseError(code: Int, errorBody: ResponseBody?): String {
        return when (code) {
            400 -> "Invalid request format"
            401 -> "Authentication failed"
            403 -> "Access denied"
            404 -> "Resource not found"
            500 -> "Internal server error"
            else -> errorBody?.string() ?: "Unknown error (code $code)"
        }
    }
}