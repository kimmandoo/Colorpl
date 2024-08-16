package com.presentation.component.adapter.reservation

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemSeatBinding
import com.domain.model.Seat

class SeatViewHolder(
    private val binding: ItemSeatBinding,
    private val onSeatSelected: (Seat) -> Unit
) : ViewHolder(binding.root) {
    fun bind(seat: Seat) {
        binding.apply {
            tvSeat.text = seat.name

            if(seat.isReserved){
                clItem.setBackgroundResource (
                    R.drawable.rectangle_calendar_temp_8
                )
            }else{
                clItem.setBackgroundResource(
                    when (seat.grade) {
                        "R" -> R.drawable.selector_timber_wolf_to_imperial_red_for_seat
                        "S" -> R.drawable.selector_timber_wolf_to_green_for_seat
                        "A" -> R.drawable.selector_timber_wolf_to_blue_for_seat
                        "B" -> R.drawable.selector_timber_wolf_to_purple_for_seat
                        else -> R.drawable.rectangle_calendar_temp_8
                    }
                )
            }
            with(seat.isSelected) {
                clItem.isSelected = this
                tvSeat.isSelected = this
            }

            tvSeat.setOnClickListener {
                if (!seat.isReserved && seat.grade != null) onSeatSelected(seat)
            }
        }
    }
}