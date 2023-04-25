package com.example.storyapp.ui.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.StoriesResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.helper.SessionPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val application: Application, private val pref: SessionPreferences) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
    val stories: LiveData<List<ListStoryItem?>?> = _stories

    private val _statusCode = MutableLiveData<Int>()
    var statusCode: LiveData<Int> = _statusCode

    fun getStories() {
        _isLoading.value = true
        val token = "Bearer ${runBlocking { pref.getToken().first() }}"
        val client = ApiConfig.getApiService().getStories(token)

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>, response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _stories.value = response.body()?.listStory
                        _statusCode.value = response.code()
                    } else {
                        _isLoading.value = false
                        Toast.makeText(application, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(application, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteSession() {
        viewModelScope.launch {
            pref.deleteSession()
        }
    }
}