package com.example.nfcdemo.network.api

import com.example.nfcdemo.model.PotteryEntity
import retrofit2.http.GET

interface PotteryApi {
    @GET("/generator/pottery/info")
    fun getById(id:String):PotteryEntity
}