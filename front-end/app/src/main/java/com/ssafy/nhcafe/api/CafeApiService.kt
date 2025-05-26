package com.ssafy.nhcafe.api

import com.ssafy.nhcafe.dto.MenuItem
import retrofit2.http.*
import retrofit2.Response

interface CafeApiService {

    @GET("/rest/product")
    suspend fun getMenuList(): Response<List<MenuItem>>


}
