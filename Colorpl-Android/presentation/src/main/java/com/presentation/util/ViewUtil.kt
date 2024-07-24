package com.presentation.util

import android.animation.ObjectAnimator
import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.view.View
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
                return@setOnEditorActionListener false
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

// View

fun setDistanceX(viewOne : View, viewTwo : View) : Float{ // 두 View 사이의 거리 구하기
    val viewOneValue =IntArray(2)
    val viewTwoValue = IntArray(2)

    viewOne.getLocationOnScreen(viewOneValue)
    viewTwo.getLocationOnScreen(viewTwoValue)

    return Math.abs(viewOneValue[0] - viewTwoValue[0]).toFloat()
}

fun View.setTransactionX(distance : Float){
    val anim = ObjectAnimator.ofFloat(this, "translationX", distance)
    anim.start()
}