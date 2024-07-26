package com.presentation.component.adapter.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemPaymentHistoryBinding
import com.domain.model.Payment
import com.presentation.base.BaseDiffUtil
import com.presentation.util.PaymentResult

class PaymentHistoryAdapter : ListAdapter<Payment, PaymentHistoryViewHolder>(
    BaseDiffUtil<Payment>()
) {
    private var onMenuClickListener: ((View, PaymentResult) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        val binding =
            ItemPaymentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.binding.ivMenu.setOnClickListener { view ->
            onMenuClickListener?.let {
                it(view, PaymentResult.getType(getItem(position).type))
            }
        }
    }

    fun setItemClickListener(listener: (View, PaymentResult) -> Unit) {
        this.onMenuClickListener = listener
    }
}