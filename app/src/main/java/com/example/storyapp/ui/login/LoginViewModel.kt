package com.example.storyapp.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.local.UserModelLogin
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.helper.SessionPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: SessionPreferences) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _statusCode = MutableLiveData<Int>()
    val statusCode: LiveData<Int> = _statusCode

    fun login(email: String, password: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().login(UserModelLogin(email, password))

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                saveSession(response.body())
                _statusCode.value = response.code()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun saveSession(response: LoginResponse?) {
        viewModelScope.launch {
            response?.let { pref.saveSession(it) }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}