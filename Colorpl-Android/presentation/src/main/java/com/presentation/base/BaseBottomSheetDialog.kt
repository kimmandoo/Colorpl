package com.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomSheetDialog<B : ViewDataBinding>(
    context: Context,
    @LayoutRes private val layoutResId: Int
) :
    BottomSheetDialog(context) {

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResId, null, false)
        setContentView(binding.root)
        setCancelable(false)
        initView()
    }

    abstract fun initView()
}