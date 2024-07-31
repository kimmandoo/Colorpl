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
            tvSeat.text = seat.toString()
            clItem.setBackgroundResource(
                when (seat.grade) {
                    1 -> R.drawable.selector_timber_wolf_to_purple_for_seat
                    2 -> R.drawable.selector_timber_wolf_to_blue_for_seat
                    3 -> R.drawable.selector_timber_wolf_to_green_for_seat
                    4 -> R.drawable.selector_timber_wolf_to_imperial_red_for_seat
                    else -> R.color.eerie_black
                }
            )
            with(seat.isSelected) {
                clItem.isSelected = this
                tvSeat.isSelected = this
            }

            tvSeat.setOnClickListener {
                if (seat.grade != 5) onSeatSelected(seat)
            }
        }
    }
}