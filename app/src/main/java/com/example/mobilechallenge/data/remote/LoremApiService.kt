package com.example.mobilechallenge.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
interface LoremApiService {
    @GET("api/lorem")
    suspend fun getLoremText(@Query("count") count: Int = 1): String
}
