package com.presentation.component.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("setImage")
fun loadImage(imageView: ImageView, Image : Drawable) {
    Glide.with(imageView.context)
        .load(Image)
        .into(imageView)
}