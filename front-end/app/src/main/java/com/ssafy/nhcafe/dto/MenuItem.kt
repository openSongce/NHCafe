package com.ssafy.nhcafe.dto

data class MenuItem(
    val name: String,
    val description: String,
    val price: String,
    val imageRes: Int,
    val category: String
)