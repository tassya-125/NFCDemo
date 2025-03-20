package com.example.nfcdemo.network


import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient  {

    private const val BASE_URL = "http://121.4.28.72:9000/"

    private val okHttpClient = OkHttpClient.Builder()
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
