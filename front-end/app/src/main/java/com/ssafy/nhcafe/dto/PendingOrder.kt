package com.ssafy.nhcafe.dto

data class PendingOrder(
    val name: String,
    val temperature: String? = null // null이면 아직 선택 안함
)
