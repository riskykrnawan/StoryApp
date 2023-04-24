package com.example.storyapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        registerViewModel = obtainViewModel(this@RegisterActivity)

        registerViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }
        registerViewModel.statusCode.observe(this) {
            if (it == 201) {
                Snackbar.make(
                    binding.contextView,
                    R.string.registration_success,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.login) {
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .show()
            } else {
                Snackbar.make(
                    binding.contextView,
                    getString(R.string.registration_failed), Snackbar.LENGTH_LONG
                )
                    .show()
            }
        }

        binding.btnRegister.setOnClickListener(this)
        binding.btnLoginNow.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                registerViewModel.register(
                    binding.edRegisterName.text.toString(),
                    binding.edRegisterEmail.text.toString(),
                    binding.edRegisterPassword.text.toString()
                )
            }

            R.id.btn_login_now -> {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): RegisterViewModel {
        return ViewModelProvider(activity)[RegisterViewModel::class.java]
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}