package com.example.nfcdemo.network.api

import com.example.nfcdemo.model.User
import com.example.nfcdemo.network.data.request.CodeRequest
import com.example.nfcdemo.network.data.request.LoginRequest
import com.example.nfcdemo.network.data.request.RegisterRequest
import com.example.nfcdemo.network.data.response.ApiResponse
import com.example.nfcdemo.network.data.response.AuthResponse
import com.example.nfcdemo.network.data.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("generator/user/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("generator/user/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/send-verification-code")
    suspend fun sendVerificationCode(@Body request: CodeRequest): Response<ApiResponse>

    @POST("/generator/user/update")
    suspend fun updateUser(@Body request: User):Response<BaseResponse<Unit>>
}