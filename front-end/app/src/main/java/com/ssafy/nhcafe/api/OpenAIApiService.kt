package com.ssafy.nhcafe.api

import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Streaming

interface OpenAIApiService {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Body request: ChatRequest
    ): ChatResponse

    @POST("v1/audio/speech")
    @Streaming
    suspend fun getSpeech(
        @Body body: TTSRequest
    ): Response<ResponseBody>
}