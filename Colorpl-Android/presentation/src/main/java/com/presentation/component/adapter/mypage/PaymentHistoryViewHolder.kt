package com.presentation.component.adapter.mypage

import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.databinding.ItemPaymentHistoryBinding
import com.domain.model.Payment
import com.presentation.util.PaymentResult

class PaymentHistoryViewHolder(
    val binding: ItemPaymentHistoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(payment: Payment) {
        binding.apply {
            type = when (payment.type) {
                0 -> PaymentResult.COMPLETE
                1 -> PaymentResult.REFUND
                else -> PaymentResult.USE
            }

        }
    }
}