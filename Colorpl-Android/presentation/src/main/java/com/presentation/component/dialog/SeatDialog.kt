package com.presentation.component.dialog

import android.content.Context
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogSeatPersonBinding
import com.presentation.base.BaseBottomSheetDialog

class SeatDialog(context: Context, private var peopleCount: Int, private val onClicked: (Int) -> Unit) :
    BaseBottomSheetDialog<DialogSeatPersonBinding>(context, R.layout.dialog_seat_person) {

    override fun initView() {
        binding.apply {
            tvCount.text = peopleCount.toString()

            tvDecrease.setOnClickListener {
                if (peopleCount > 1) {
                    peopleCount--
                    tvCount.text = peopleCount.toString()
                }
            }

            tvIncrease.setOnClickListener {
                if (peopleCount < 8) {
                    peopleCount++
                    tvCount.text = peopleCount.toString()
                }
            }
            tvConfirmPersonCount.setOnClickListener {
                onClicked(peopleCount)
                dismiss()
            }
        }
    }
}