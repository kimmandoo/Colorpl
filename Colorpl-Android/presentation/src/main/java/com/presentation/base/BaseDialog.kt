package com.presentation.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseDialog<T : ViewDataBinding>(
    private val context: Context,
    @LayoutRes private val layoutResId: Int
) {

    protected lateinit var binding: T
    protected lateinit var dialog: AlertDialog

    init {
        initDialog()
    }

    private fun initDialog() {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, layoutResId, null, false)

        val builder = AlertDialog.Builder(context)
            .setView(binding.root)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    abstract fun onCreateDialog()

    fun show() {
        onCreateDialog()
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun setCancelable(cancelable: Boolean): BaseDialog<T> {
        dialog.setCancelable(cancelable)
        return this
    }
}