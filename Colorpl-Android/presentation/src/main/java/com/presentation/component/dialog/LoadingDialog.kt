package com.presentation.component.dialog

import android.content.Context
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogLoadingBinding
import com.presentation.base.BaseDialog

class LoadingDialog(context: Context): BaseDialog<DialogLoadingBinding>(context, R.layout.dialog_loading) {
    override fun onCreateDialog() {
        this.setCancelable(false)
    }


}