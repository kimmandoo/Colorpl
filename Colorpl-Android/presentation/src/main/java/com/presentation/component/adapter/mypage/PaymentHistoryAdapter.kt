package com.presentation.component.adapter.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemPaymentHistoryBinding
import com.domain.model.Payment
import com.presentation.base.BaseDiffUtil

class PaymentHistoryAdapter : ListAdapter<Payment, PaymentHistoryViewHolder>(
    BaseDiffUtil<Payment>()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        val binding =
            ItemPaymentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}