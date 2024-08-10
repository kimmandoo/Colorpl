package com.presentation.component.adapter.mypage

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemPaymentHistoryBinding
import com.domain.model.PayReceipt
import com.presentation.util.PaymentResult
import timber.log.Timber

class PaymentHistoryViewHolder(
    val binding: ItemPaymentHistoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(payment: PayReceipt) {
        binding.apply {
            type = setPaymentResultType(payment.statusLocale, binding.root.context)
            Timber.d("데이터 확인여 $type")
            payReceipt = payment
        }
    }

    fun setPaymentResultType(value: String, context: Context): PaymentResult {
        return if (value == context.getString(R.string.my_page_payment_complete)) {
            PaymentResult.COMPLETE
        } else {
            PaymentResult.REFUND
        }
    }
}