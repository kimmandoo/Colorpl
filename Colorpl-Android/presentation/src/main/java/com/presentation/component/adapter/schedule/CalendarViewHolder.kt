package com.presentation.component.adapter.schedule

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemCalendarBinding
import com.domain.model.CalendarItem

class CalendarViewHolder(
    private val binding: ItemCalendarBinding,
    private val onItemClick: (CalendarItem) -> Unit,
) : ViewHolder(binding.root) {
    fun bind(data: CalendarItem) {
        binding.apply {
//            Glide.with(binding.root.context).load("null").into(ivTicket)
            ivTicket.visibility = if(data.isWeek){
                View.VISIBLE
            }else{
                View.GONE
            }
            clItem.setBackgroundResource(
                when {
                    data.isSelected -> R.drawable.rectangle_imperial_red_8
                    data.isToday -> R.drawable.rectangle_calendar_temp_8_stroke_4_imperial_red
                    else -> R.drawable.rectangle_calendar_temp_8
                }
            )
            tvDate.setOnClickListener {
                if (data.isCurrentMonth) onItemClick(data)
            }
            tvDate.apply {
                text = data.date.dayOfMonth.toString()
                setTextColor(
                    when {
                        data.isSelected -> Color.WHITE
                        data.isSunday -> Color.RED
                        else -> Color.WHITE
                    }
                )
                alpha = if (data.isCurrentMonth) 1f else 0.3f
            }
        }
    }
}