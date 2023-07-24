package com.example.slide.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface SlideService {
    @GET
    suspend fun getSlideData(@Url url: String): Response<SlideResponse>
}