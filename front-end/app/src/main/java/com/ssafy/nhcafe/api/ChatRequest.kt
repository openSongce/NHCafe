package com.ssafy.nhcafe.api

data class ChatRequest(
    val model: String = "gpt-4o",
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)
