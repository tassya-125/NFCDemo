package com.example.nfcdemo.network.api


import com.example.nfcdemo.network.data.response.PotteryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PotteryApi {
    @GET("/generator/pottery/info/{uid}")
    suspend fun getById(@Path("uid") uid:String): Response<PotteryResponse>
}