package com.presentation.component.adapter.reservation.process

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemDateTableBinding
import com.domain.model.DateTableItem
import timber.log.Timber
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import android.content.Context

class ReservationDateTableViewHolder(
    private val binding: ItemDateTableBinding,
    private val onItemClick: (DateTableItem) -> Unit,
) : ViewHolder(binding.root) {
    fun bind(data: DateTableItem) {
        binding.apply {
            Timber.d("${data.date.dayOfWeek}")
            clItem.setBackgroundResource(
                when {
                    data.isSelected -> R.drawable.rectangle_imperial_red
                    data.isEvent.not() -> R.drawable.rectangle_eerie_black
                    else -> R.drawable.rectangle_timber_wolf
                }
            )

            clItem.setOnClickListener {
                if (data.isEvent) onItemClick(data)
            }
            tvDate.apply {
                text = setDay(data.date)
                setTextColor(
                    when {
                        data.isSelected -> context.getColor(R.color.white)
                        data.date.dayOfWeek == java.time.DayOfWeek.SATURDAY -> context.getColor(R.color.blue)
                        data.date.dayOfWeek == java.time.DayOfWeek.SUNDAY -> context.getColor(R.color.imperial_red)
                        data.isEvent.not() -> context.getColor(R.color.light_gray)
                        else -> context.getColor(R.color.eerie_black)
                    }
                )
            }
            tvDaysOfWeek.apply {
                text = setDayOfWeek(context, data.date)
                setTextColor(
                    when {
                        data.isSelected -> context.getColor(R.color.white)
                        data.date.dayOfWeek == java.time.DayOfWeek.SATURDAY -> context.getColor(R.color.blue)
                        data.date.dayOfWeek == java.time.DayOfWeek.SUNDAY -> context.getColor(R.color.imperial_red)
                        data.isEvent.not() -> context.getColor(R.color.light_gray)
                        else -> context.getColor(R.color.eerie_black)
                    }
                )
            }

        }
    }

    private fun setDay(date: LocalDate): String {
        return if (date.dayOfMonth == 1) {
            "${date.monthValue}.${date.dayOfMonth}"
        } else {
            date.dayOfMonth.toString()
        }
    }

    private fun setDayOfWeek(context: Context, date: LocalDate): String {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        return when (date) {
            today -> context.getString(R.string.reservation_date_today)
            tomorrow -> context.getString(R.string.reservation_date_tomorrow)
            else -> date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        }
    }
}
