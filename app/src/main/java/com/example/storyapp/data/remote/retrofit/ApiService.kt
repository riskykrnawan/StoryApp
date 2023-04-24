package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.local.UserModelLogin
import com.example.storyapp.data.local.UserModelRegister
import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.response.StoriesResponse
import com.example.storyapp.data.remote.response.Story
import com.example.storyapp.data.remote.response.SuccessResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/v1/register")
    fun register(
        @Body userModel: UserModelRegister
    ): Call<SuccessResponse>

    @POST("/v1/login")
    fun login(
        @Body userModel: UserModelLogin
    ): Call<LoginResponse>

    @GET("/v1/stories")
    fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0,
    ): Call<StoriesResponse>

    @GET("/v1/stories/{id}")
    fun getStoryById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
    ): Call<DetailStoryResponse>

    @POST("/v1/stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Header("Authorization") authorization: String,
        @Part("description") description: RequestBody,
    ): Call<DetailStoryResponse>
}