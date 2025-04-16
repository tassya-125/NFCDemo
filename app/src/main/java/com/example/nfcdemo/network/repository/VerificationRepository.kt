package com.example.nfcdemo.network.repository

import android.util.Log
import com.example.nfcdemo.network.RetrofitClient
import com.example.nfcdemo.network.api.VerificationHistoryApi
import com.example.nfcdemo.network.data.request.VerificationHistoryRequest
import com.example.nfcdemo.network.data.response.BaseResponse

import com.example.nfcdemo.util.NetworkUtil.parseError

object VerificationRepository {

    val service = RetrofitClient.instance.create(VerificationHistoryApi::class.java)

    private val TAG = "VerificationHistory_NET"

    suspend fun save(verificationHistoryRequest: VerificationHistoryRequest):Result<BaseResponse<Unit>>{
        return try{
            val response= service.saveVerificationHistory(verificationHistoryRequest)
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