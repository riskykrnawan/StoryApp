package com.example.storyapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.helper.SessionPreferences
import com.example.storyapp.helper.Validation
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sessions")

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setMyButtonEnable()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        registerViewModel = obtainViewModel(this@RegisterActivity, pref)

        registerViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }
        registerViewModel.statusCode.observe(this) {
            if (it == 201) {
                Snackbar.make(
                    binding.contextView, R.string.registration_success, Snackbar.LENGTH_LONG
                ).setAction(R.string.login) {
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }.show()
            } else {
                Snackbar.make(
                    binding.contextView,
                    getString(R.string.registration_failed),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        binding.btnLoginNow.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnRegister.setOnClickListener {
            registerViewModel.register(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
        }


        binding.edRegisterEmail.addTextChangedListener(textWatcher)
        binding.edRegisterPassword.addTextChangedListener(textWatcher)
    }

    private fun setMyButtonEnable() {
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        binding.btnRegister.isEnabled = Validation.isValidForm(email, password)
    }

    private fun obtainViewModel(
        activity: AppCompatActivity, pref: SessionPreferences
    ): RegisterViewModel {
        val factory = ViewModelFactory(activity.application, pref)
        return ViewModelProvider(activity, factory)[RegisterViewModel::class.java]
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}