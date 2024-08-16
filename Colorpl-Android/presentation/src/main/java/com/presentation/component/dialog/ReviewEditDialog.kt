package com.presentation.component.dialog

import android.content.Context
import android.widget.Toast
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogEditBinding
import com.presentation.base.BaseDialog

class ReviewEditDialog(
    context: Context,
    val originText: String,
    val onSaveClickListener: (String) -> Unit,
) :
    BaseDialog<DialogEditBinding>(context, R.layout.dialog_edit) {
    override fun onCreateDialog() {
        binding.apply {
            etEdit.setText(originText)
            tvEdit.setOnClickListener {
                etEdit.text.toString().run {
                    if (isNotEmpty()) {
                        onSaveClickListener(this)
                    } else {
                        Toast.makeText(binding.root.context, "내용이 비어있습니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            tvCancel.setOnClickListener {
                Toast.makeText(binding.root.context, "수정을 취소합니다", Toast.LENGTH_SHORT)
                    .show()
                dismiss()
            }
        }
    }
}