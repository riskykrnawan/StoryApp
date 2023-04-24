package com.example.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.SessionPreferences
import com.example.storyapp.helper.Validation
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.home.HomeActivity
import com.example.storyapp.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("sessions")

class LoginActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setMyButtonEnable()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        val loginViewModel = obtainViewModel(this@LoginActivity, pref)

        loginViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }

        binding.btnRegisterNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            loginViewModel.login(email, password)
        }

        loginViewModel.statusCode.observe(this) {
            if (loginViewModel.statusCode.value == 200) {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Snackbar.make(
                    binding.contextView, getString(R.string.login_failed), Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.edLoginEmail.addTextChangedListener(textWatcher)
        binding.edLoginPassword.addTextChangedListener(textWatcher)
    }

    private fun setMyButtonEnable() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        binding.btnLogin.isEnabled = Validation.isValidForm(email, password)
    }


    private fun obtainViewModel(
        activity: AppCompatActivity, pref: SessionPreferences
    ): LoginViewModel {
        val factory = ViewModelFactory(activity.application, pref)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}