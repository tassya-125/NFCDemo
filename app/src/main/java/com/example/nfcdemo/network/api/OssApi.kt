package com.example.nfcdemo.network.api

import com.example.nfcdemo.network.data.response.OssSignatureResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface OssApi {
    @GET("oss/generate-signature")
    suspend fun getOssSignature(): Response<OssSignatureResponse>


    @Multipart
    @POST
    suspend fun uploadToOss(
        @Url url: String,  // 动态URL，使用从签名响应中生成的上传地址
        @Part policy: MultipartBody.Part,
        @Part signature: MultipartBody.Part,
        @Part accessKeyId: MultipartBody.Part,
        @Part key: MultipartBody.Part,
        @Part file: MultipartBody.Part,
    ): Response<ResponseBody>

}