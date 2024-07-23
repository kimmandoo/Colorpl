package com.presentation.util

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide


// EditText

fun EditText.imeOptionsActionCheck(action: () -> Unit) {
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

fun EditText.setPasswordTransformation(){
    if(this.transformationMethod == null){
        this.transformationMethod = PasswordTransformationMethod.getInstance()
    }else{
        this.transformationMethod = null
    }
}

// ImageView
fun ImageView.setImage(image : Any){
    Glide.with(this)
        .load(image)
        .circleCrop()
        .into(this)
}