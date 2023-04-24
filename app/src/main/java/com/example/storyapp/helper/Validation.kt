package com.example.storyapp.helper

import android.util.Patterns

object Validation {
    fun isValidEmail(email: CharSequence): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun isValidForm(email: String, password: String): Boolean {
        return isValidEmail(email) && password.length >= 8
    }
}