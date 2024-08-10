package com.presentation.component.adapter.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.colorpl.presentation.databinding.ItemPaymentHistoryBinding
import com.domain.model.PayReceipt
import com.presentation.base.BaseDiffUtil
import com.presentation.util.PaymentResult

class PaymentHistoryAdapter : ListAdapter<PayReceipt, PaymentHistoryViewHolder>(
    BaseDiffUtil<PayReceipt>()
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
                it(
                    view,
                    holder.setPaymentResultType(
                        getItem(position).statusLocale,
                        holder.binding.root.context
                    )
                )
            }
        }
    }

    fun setItemClickListener(listener: (View, PaymentResult) -> Unit) {
        this.onMenuClickListener = listener
    }


    companion object {
        const val COMPLETE = 0
        const val REFUND = 1
    }
}