package com.example.storyapp.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.example.storyapp.R
import com.example.storyapp.helper.SessionPreferences
import com.example.storyapp.ui.home.HomeActivity
import com.example.storyapp.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sessions")

class SplashActivity : AppCompatActivity() {

//    private var isLogin: Flow<Boolean> = dataStore.data.map { preferences ->
//        preferences.contains(stringPreferencesKey("session_token"))
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Default).launch {
            delay(THREE_SECOND)

//            if (!isLogin.asLiveData().value) {
//                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }

            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val THREE_SECOND = 3000L
    }
}