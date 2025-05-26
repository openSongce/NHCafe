package com.ssafy.nhcafe.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object CafeApiClient {
    private const val BASE_URL = "http://15.165.88.87:9987"  // 너가 만든 백엔드 주소

    private val client = OkHttpClient.Builder().build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: CafeApiService = retrofit.create(CafeApiService::class.java)
}
