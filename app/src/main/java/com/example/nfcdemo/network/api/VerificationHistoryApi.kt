package com.example.nfcdemo.network.api

import com.example.nfcdemo.network.data.response.VerificationHistoryListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface VerificationHistoryApi {
    @GET("/generator/verificationhistory/getByUserList")
    suspend fun getList(@QueryMap params: Map<String, String>): Response<VerificationHistoryListResponse>
}