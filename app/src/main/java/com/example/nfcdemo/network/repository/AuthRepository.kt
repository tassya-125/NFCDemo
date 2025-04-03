package com.example.nfcdemo.network.repository

import com.example.nfcdemo.network.RetrofitClient
import com.example.nfcdemo.network.api.AuthApi
import com.example.nfcdemo.network.data.request.CodeRequest
import com.example.nfcdemo.network.data.request.LoginRequest
import com.example.nfcdemo.network.data.request.RegisterRequest
import com.example.nfcdemo.network.data.response.AuthResponse
import com.example.nfcdemo.util.NetworkUtil.parseError
import okhttp3.ResponseBody

class AuthRepository {
    private val apiService = RetrofitClient.instance.create(AuthApi::class.java)

    suspend fun login(identifier: String, password: String): Result<AuthResponse> {
        return try {
            val response =apiService.login(LoginRequest(identifier, password))
            when {
                response.isSuccessful && response.body() != null -> {
                    Result.success(response.body()!!)
                }
                response.isSuccessful -> {
                    Result.failure(Exception("Empty response body"))
                }
                else -> {
                    val errorMsg = parseError(response.code(), response.errorBody())
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message ?: "Unknown error"}"))
        }
    }



    suspend fun register(identifier: String, password: String, code: String,isUsingPhone:Boolean): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(identifier, password, code,isUsingPhone))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("注册失败: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("网络错误: ${e.message}"))
        }
    }

    suspend fun sendVerificationCode(identifier: String): Result<Unit> {
        return try {
            val response = apiService.sendVerificationCode(CodeRequest(identifier))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("发送验证码失败: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("网络错误: ${e.message}"))
        }
    }
}
