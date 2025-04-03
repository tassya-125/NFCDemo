package com.example.nfcdemo.network.repository

import android.util.Log
import com.example.nfcdemo.network.data.response.PotteryResponse
import com.example.nfcdemo.network.RetrofitClient
import com.example.nfcdemo.network.api.PotteryApi
import com.example.nfcdemo.util.NetworkUtil.parseError

object PotteryRepository {

    private val potteryService = RetrofitClient.instance.create(PotteryApi::class.java)

    private val TAG ="POTTERY_NET"

    suspend fun getInfo(uid:String):Result<PotteryResponse>{
       return try{
            Log.d(TAG,uid)
            val response= potteryService.getById(uid)
            Log.d(TAG, response.toString())
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
        }catch (e:Exception){
            Log.e(TAG,"Network error: ${e.message ?: "Unknown error"}")
            Result.failure(Exception("Network error: ${e.message ?: "Unknown error"}"))
        }
    }
}