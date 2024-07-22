package com.presentation.component.adapter

import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.presentation.util.Sign

@BindingAdapter("setImage")
fun loadImage(imageView: ImageView, Image : Drawable?) {
    Glide.with(imageView.context)
        .load(Image)
        .into(imageView)
}

@BindingAdapter("setSignHint")
fun setSignHint(editText : EditText, type : Sign?){
    val context = editText.context
    type?.run {
        val text = when(type){
            Sign.ID -> {
                context.getString(R.string.login_id_hint)
            }
            Sign.PASSWORD -> {
                context.getString(R.string.login_password_hint)
            }
            else -> {
                context.getString(R.string.login_nickname_hint)
            }
        }
        editText.setHint(text)
    }
}

@BindingAdapter("setSignTitle")
fun setSignTitle(textView : TextView, type : Sign?){
    val context = textView.context
    type?.run {
        val text = when(type){
            Sign.ID -> {
                context.getString(R.string.sign_up_id)
            }
            Sign.PASSWORD -> {
                context.getString(R.string.sign_up_password)
            }
            else -> {
                context.getString(R.string.sign_up_nickname)
            }
        }
        textView.setText(text)
    }
}

@BindingAdapter("setSignMark")
fun setSignMark(imageView : ImageView , type : Sign?){
    val context = imageView.context
    type?.run {
        val image = when(type){
            Sign.ID -> {
                ContextCompat.getDrawable(context, R.drawable.ic_id_mark)
            }
            Sign.PASSWORD -> {
                ContextCompat.getDrawable(context, R.drawable.ic_password_mark)
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.ic_nickname_mark)
            }
        }
        loadImage(imageView, image)
    }
}
