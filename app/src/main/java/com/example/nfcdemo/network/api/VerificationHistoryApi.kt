package com.example.nfcdemo.network.api


import com.example.nfcdemo.network.data.request.VerificationHistoryRequest
import com.example.nfcdemo.network.data.response.BaseResponse
import com.example.nfcdemo.network.data.response.VerificationHistoryListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface VerificationHistoryApi {
    @GET("/generator/verificationhistory/getByUserList")
    suspend fun getList(@QueryMap params: Map<String, String>): Response<VerificationHistoryListResponse>

    @POST("/generator/verificationhistory/save")
    suspend fun saveVerificationHistory(@Body request : VerificationHistoryRequest):Response<BaseResponse<Unit>>
}