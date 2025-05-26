package com.ssafy.nhcafe.dto

data class OrderDetail(val productId: Int, val quantity: Int, val id: Int = 0, val orderId: Int = 0)

data class OrderRequest(
    val id: Int = 0,
    val userId: String?,
    val completed:String ="N",// 전화번호 없으면 null 또는 "" 로 보낼 것
    val usedStamp: Int,
    val price: Int,
    val details: List<OrderDetail>
)

data class OrderResponse(
    val id: Int
)
