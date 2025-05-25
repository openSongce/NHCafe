package com.ssafy.nhcafe.api

data class TTSRequest(
    val model: String = "gpt-4o-mini-tts",
    val input: String,
    val voice: String = "coral",
    val response_format: String = "mp3"
)
