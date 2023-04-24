package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.local.UserModelLogin
import com.example.storyapp.data.local.UserModelRegister
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.SuccessResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/v1/register")
    fun register(
        @Body userModel: UserModelRegister
    ): Call<SuccessResponse>
    @POST("/v1/login")
    fun login(
        @Body userModel: UserModelLogin
    ): Call<LoginResponse>

    @POST("/v1/login")
    fun getStories(
        @Body userModel: UserModelLogin
    ): Call<LoginResponse>
}