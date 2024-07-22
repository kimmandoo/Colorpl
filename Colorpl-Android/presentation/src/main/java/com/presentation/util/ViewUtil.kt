package com.presentation.util

import android.content.Context
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.EditText

fun EditText.imeOptionsActionCheck(context: Context, action: () -> Unit) {
    this.setOnEditorActionListener { textView, actionId, keyEvent ->
        when (actionId) {
            IME_ACTION_DONE -> {
                action()
                return@setOnEditorActionListener true
            }

            IME_ACTION_NEXT -> {
                action()
                return@setOnEditorActionListener true
            }
        }
        return@setOnEditorActionListener false
    }
}
