package com.maurolombardi.devigetkotlin.interfaces

import com.maurolombardi.devigetkotlin.dto.dtos
import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("/r/all/top.json")
    fun getReddits(): Call<dtos.RedditJsonResponse>
}