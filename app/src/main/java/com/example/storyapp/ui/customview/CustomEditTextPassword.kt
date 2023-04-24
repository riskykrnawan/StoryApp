package com.example.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputLayout


class CustomEditTextPassword : AppCompatEditText {

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 8) {
                    error = context.getString(R.string.password_warning)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }
}