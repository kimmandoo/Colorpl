package com.presentation.component.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.View
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogCalendarBinding
import com.presentation.base.BaseDialog

class CalendarDialog(context: Context, private val onConfirmClicked: (Long, Long) -> Unit) :
    BaseDialog<DialogCalendarBinding>(context, R.layout.dialog_calendar) {
    private var year: Int = 0
    private var month: Int = 0

    override fun onCreateDialog() {
        binding.apply {
            hideDaySpinner()
            tvConfirm.setOnClickListener {
                onConfirmClicked(year.toLong(), (month).toLong())
                dismiss()
            }
            dpCalendar.setOnDateChangedListener { view, yearValue, monthValue, dayOfMonth ->
                year = yearValue
                month = monthValue
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    /**
     * Spinner에서 Day를 제거해주는 함수
     */
    @SuppressLint("DiscouragedApi")
    private fun hideDaySpinner() {
        val daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android")
        if (daySpinnerId != 0) {
            val daySpinner = binding.dpCalendar.findViewById<View>(daySpinnerId)
            daySpinner?.visibility = View.GONE
        }

        val dayHeaderId =
            Resources.getSystem().getIdentifier("date_picker_header_day", "id", "android")
        if (dayHeaderId != 0) {
            val dayHeader = binding.dpCalendar.findViewById<View>(dayHeaderId)
            dayHeader?.visibility = View.GONE
        }
    }
}