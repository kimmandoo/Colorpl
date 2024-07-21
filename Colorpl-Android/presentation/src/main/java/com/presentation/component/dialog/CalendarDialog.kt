package com.presentation.component.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.NumberPicker
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogCalendarBinding
import com.presentation.base.BaseDialog
import java.util.Calendar

class CalendarDialog(context: Context) :
    BaseDialog<DialogCalendarBinding>(context, R.layout.dialog_calendar) {

    private lateinit var yearPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker

    override fun onCreateDialog() {
        binding.apply {
            hideDaySpinner()
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