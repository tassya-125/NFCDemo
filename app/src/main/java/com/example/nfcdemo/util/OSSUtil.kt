package com.example.nfcdemo.util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.example.nfcdemo.network.RetrofitClient
import com.example.nfcdemo.network.api.OssApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.UUID



object OSSUtil {
    private val ossService = RetrofitClient.instance.create(OssApi::class.java)

    private val TAG :String = "OSS"

    suspend fun uploadImageToOss(uri: Uri, context: Context): String? {
        val signatureResponse = ossService.getOssSignature().body()

        if (signatureResponse != null) {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()

            val filename = "profile_${System.currentTimeMillis()}.jpg"
            val ossUrl = "${signatureResponse.host}/${signatureResponse.dir}$filename"

            // 创建 MultipartBody.Part
            val policyPart = MultipartBody.Part.createFormData("policy", signatureResponse.policy)
            val signaturePart = MultipartBody.Part.createFormData("signature", signatureResponse.signature)
            val accessKeyIdPart = MultipartBody.Part.createFormData("OSSAccessKeyId", signatureResponse.accessKeyId)
            val keyPart = MultipartBody.Part.createFormData("key", "${signatureResponse.dir}$filename")
            val filePart = MultipartBody.Part.createFormData(
                "file", filename, RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray!!)
            )
            try {
                // 发起上传请求
                val response = ossService.uploadToOss(
                    url = signatureResponse.host,
                    policy = policyPart,
                    signature = signaturePart,
                    accessKeyId = accessKeyIdPart,
                    key = keyPart,
                    file = filePart
                )

                if (response.isSuccessful) {
                    Log.d(TAG,"上传成功！文件路径: $ossUrl")
                    return ossUrl
                } else {
                    Log.e(TAG,"上传失败: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG,"上传过程中发生错误: ${e.message}")
            }
        }
        Log.e(TAG,"获取OSS凭证错误")
        return null
    }
}
