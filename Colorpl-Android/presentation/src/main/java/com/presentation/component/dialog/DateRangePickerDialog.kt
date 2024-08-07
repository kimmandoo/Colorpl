package com.presentation.component.dialog

import android.content.Context
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogDateRangePickerBinding
import com.presentation.base.BaseDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateRangePickerDialog(context: Context, private val onConfirmClicked: (Long, Long, Long) -> Unit) :
    BaseDialog<DialogDateRangePickerBinding>(context, R.layout.dialog_date_range_picker) {
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun onCreateDialog() {
        val today = Calendar.getInstance()
        val twoWeeksLater = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 13) }

        binding.apply {
            dpCalendar.minDate = today.timeInMillis
            dpCalendar.maxDate = twoWeeksLater.timeInMillis

            tvConfirm.setOnClickListener {
                onConfirmClicked(year.toLong(), (month + 1).toLong(), day.toLong()) // month is 0-based
                dismiss()
            }
            dpCalendar.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) { _, yearValue, monthValue, dayValue ->
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
