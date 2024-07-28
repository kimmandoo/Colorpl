package com.presentation.component.adapter.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.colorpl.presentation.databinding.ItemReservationInfoBinding
import com.domain.model.ReservationInfo
import com.presentation.base.BaseDiffUtil

class ReservationInfoAdapter (
    private val onClickListener: () -> Unit,
) : ListAdapter<ReservationInfo, ViewHolder>(BaseDiffUtil<ReservationInfo>()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemReservationInfoBinding.inflate(layoutInflater, parent, false)
        return ReservationInfoViewHolder(
            binding,
            onClickListener = onClickListener,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ReservationInfoViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

}