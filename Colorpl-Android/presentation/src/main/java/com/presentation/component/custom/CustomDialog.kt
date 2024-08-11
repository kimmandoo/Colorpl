package com.presentation.component.custom

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogCancelableBinding
import com.presentation.util.getSizeX

class CustomDialog(val context: Context) {
    val dialog = Dialog(context)

    fun cancellableDialog(
        dialogText: String,
        complete: () -> Unit,
        cancel: () -> Unit,
    ) {
        val binding = DataBindingUtil.inflate<DialogCancelableBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_cancelable,
            null,
            false
        )

        binding.dialogText = dialogText

        binding.tvCustomDialogCancel.setOnClickListener {
            cancel()
            dialog.dismiss()
        }
        binding.tvCustomDialogComplete.setOnClickListener {
            complete()
            dialog.dismiss()
        }


        dialog.setContentView(binding.root)
        dialog.window.apply {
            this?.setLayout(
                (getSizeX(context) * 0.84).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.window?.setBackgroundDrawableResource(R.drawable.rectangle_white_10)
        dialog.show()
    }
}