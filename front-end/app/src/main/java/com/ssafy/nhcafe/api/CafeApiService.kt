package com.ssafy.nhcafe.api

import com.ssafy.nhcafe.dto.MenuItem
import com.ssafy.nhcafe.dto.OrderRequest
import com.ssafy.nhcafe.dto.OrderResponse
import com.ssafy.nhcafe.dto.UserInfo
import com.ssafy.nhcafe.dto.UserRequest
import retrofit2.http.*
import retrofit2.Response

interface CafeApiService {

    @GET("/rest/product")
    suspend fun getMenuList(): Response<List<MenuItem>>

    @POST("/rest/order")
    suspend fun placeOrder(
        @Body orderRequest: OrderRequest
    ): Response<Int>

    @POST("/rest/user/number")
    suspend fun getOrCreateUser(@Body request: UserRequest): Response<Int>

}
