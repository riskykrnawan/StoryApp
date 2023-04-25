package com.example.storyapp.ui.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.response.SuccessResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.helper.SessionPreferences
import com.example.storyapp.helper.Utils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryViewModel(private val pref: SessionPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    // message response body sulit dibaca jadi buat response berdasarkan statusCode
    private val _statusCode = MutableLiveData<Int>()
    val statusCode: LiveData<Int> = _statusCode

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun postStory(file: File, description: String) {
        _isLoading.value = true

        val token = "Bearer ${runBlocking { pref.getToken().first() }}"

        val reducedFile = Utils.reduceFileImage(file)
        val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())

        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", reducedFile.name, requestImageFile
        )

        val client = ApiConfig.getApiService().postStory(token, imageMultipart, requestDescription)

        client.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>, response: Response<SuccessResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _statusCode.value = response.code()
                    }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }

        })

    }
}