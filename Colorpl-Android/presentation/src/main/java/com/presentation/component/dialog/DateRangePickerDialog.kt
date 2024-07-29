package com.presentation.component.dialog

import android.content.Context
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogDateRangePickerBinding
import com.presentation.base.BaseDialog

class DateRangePickerDialog(context: Context, private val onConfirmClicked: (Long, Long, Long) -> Unit) :
    BaseDialog<DialogDateRangePickerBinding>(context, R.layout.dialog_date_range_picker) {
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun onCreateDialog() {
        binding.apply {
            tvConfirm.setOnClickListener {
                onConfirmClicked(year.toLong(), (month).toLong(), day.toLong())
                dismiss()
            }
            dpCalendar.setOnDateChangedListener { view, yearValue, monthValue, dayValue ->
                year = yearValue
                month = monthValue
                day = dayValue
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

}
