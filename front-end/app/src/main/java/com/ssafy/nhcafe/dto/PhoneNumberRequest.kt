package com.ssafy.nhcafe.dto

data class UserRequest(
    val number: String,
    val stamps: Int = 0,
    val stampList: List<StampDetail> = emptyList()
)

data class StampDetail(
    val id: Int = 0,
    val userId: String = "",
    val orderId: Int = 0,
    val quantity: Int = 0
)

data class UserInfo(
    val id: Int,
    val number: String,
    val stamps: Int,
    val stampList: List<StampDetail>
)

