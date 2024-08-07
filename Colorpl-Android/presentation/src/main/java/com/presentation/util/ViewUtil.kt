package com.presentation.util

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.colorpl.presentation.BuildConfig
import timber.log.Timber
import kotlin.math.abs


// EditText

fun EditText.imeOptionsActionCheck(action: () -> Unit) {
    this.setOnEditorActionListener { textView, actionId, keyEvent ->
        textView.context.hideKeyboard(textView)
        when (actionId) {
            IME_ACTION_DONE -> {
                action()
                return@setOnEditorActionListener false
            }

            IME_ACTION_NEXT -> {
                action()
                return@setOnEditorActionListener true
            }

            IME_ACTION_SEARCH -> {
                action()
                return@setOnEditorActionListener true
            }

        }
        return@setOnEditorActionListener false
    }
}

fun EditText.setPasswordTransformation() {
    if (this.transformationMethod == null) {
        this.transformationMethod = PasswordTransformationMethod.getInstance()
    } else {
        this.transformationMethod = null
    }
}

//
/*
    ImageView -> 서버 통신 용임
    type -> true : 서버 -> false : 로컬
 */
fun ImageView.setImage(image: Any?, type : Boolean = false) {
    val data = if(type){
        BuildConfig.IMAGE_BASE+image
    }else{
        image
    }
    Glide.with(this)
        .load(data)
        .circleCrop()
        .into(this)
}

// View

fun setDistanceX(viewOne: View, viewTwo: View): Float { // 두 View 사이의 거리 구하기
    val viewOneValue = IntArray(2)
    val viewTwoValue = IntArray(2)

    viewOne.getLocationOnScreen(viewOneValue)
    viewTwo.getLocationOnScreen(viewTwoValue)

    return Math.abs(viewOneValue[0] - viewTwoValue[0]).toFloat()
}

fun View.setTransactionX(distance: Float) {
    val anim = ObjectAnimator.ofFloat(this, "translationX", distance)
    anim.start()
}

//ViewPager
fun ViewPager2.addCustomItemDecoration() {
    this.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = 70.dpToPx
            outRect.left = 70.dpToPx
        }
    })
    val scaleFactor = 0.8f

    val pageTranslationX = 150.dpToPx

    this.setPageTransformer { page, position ->
        page.translationX = -pageTranslationX * (position)

        if (position <= -1 || position >= 1) {
            // 페이지가 보이지 않을 때
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        }else{
            // 왼쪽 페이지 && 오른쪽 페이지
            val absolute = 1 - abs(position)
            page.scaleX = scaleFactor + (1 - scaleFactor) * absolute
            page.scaleY = scaleFactor + (1 - scaleFactor) * absolute
        }

        }
}