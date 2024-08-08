package com.presentation.component.dialog

import android.content.Context
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogDateRangePickerBinding
import com.presentation.base.BaseDialog
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class DateRangePickerDialog(
    context: Context,
    private val initialDate: LocalDate,
    private val onConfirmClicked: (Int, Int, Int) -> Unit
) : BaseDialog<DialogDateRangePickerBinding>(context, R.layout.dialog_date_range_picker) {
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun onCreateDialog() {
        val today = Calendar.getInstance()
        val twoWeeksLater = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 13) }
        Timber.tag("년월일").d("년: $year 월: $month 일: $day")
        binding.apply {
            dpCalendar.minDate = today.timeInMillis
            dpCalendar.maxDate = twoWeeksLater.timeInMillis

            tvConfirm.setOnClickListener {
                onConfirmClicked(year, month+1, day)
                dismiss()
            }

            dpCalendar.setOnDateChangedListener { _, yearValue, monthValue, dayValue ->
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
