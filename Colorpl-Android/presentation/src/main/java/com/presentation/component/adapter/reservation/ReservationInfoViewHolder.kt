package com.presentation.component.adapter.reservation

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemReservationInfoBinding
import com.domain.model.ReservationInfo
import com.presentation.util.Category

class ReservationInfoViewHolder(
    private val binding: ItemReservationInfoBinding,
    private val onClickListener: (ReservationInfo) -> Unit,
) : ViewHolder(binding.root){

    fun bind(data: ReservationInfo) {
        binding.apply {
            clReservationInfo.setOnClickListener{
                onClickListener(data)
            }
            reservationInfo = data
            tvCategory.text = Category.getKeyResult(data.category.toString())
        }
    }
}