package com.presentation.component.adapter.mypage

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.ItemPaymentHistoryBinding
import com.domain.mapper.toSeatList
import com.domain.model.PayReceipt
import com.presentation.util.PaymentResult
import com.presentation.util.formatIsoToKorean
import com.presentation.util.formatWithCommas
import com.presentation.util.setImageCenterCrop

class PaymentHistoryViewHolder(
    val binding: ItemPaymentHistoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(payment: PayReceipt) {
        binding.apply {
            type = setPaymentResultType(payment.statusLocale, binding.root.context)
            ivImage.setImageCenterCrop(payment.showDetailPosterImagePath)
            price = payment.price.formatWithCommas()
            seat = payment.seatInfoDto.toSeatList()
            payReceipt = payment
            tvTimeValue.text = payment.showDateTime.formatIsoToKorean()
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