package com.presentation.my_page

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentPaymentHistoryBinding
import com.domain.model.Payment
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.mypage.PaymentHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentHistoryFragment :
    BaseFragment<FragmentPaymentHistoryBinding>(R.layout.fragment_payment_history) {

    val paymentHistoryAdapter: PaymentHistoryAdapter by lazy {
        PaymentHistoryAdapter()
    }

    override fun initView() {
        initAdapter()
        initClickEvent()
    }

    private fun initAdapter() {
        binding.rcPaymentHistory.apply {
            adapter = paymentHistoryAdapter
            itemAnimator = null
        }

        paymentHistoryAdapter.submitList(
            listOf(Payment(0), Payment(1), Payment(2))
        )
    }

    private fun initClickEvent() {
        binding.includeTop.ivBack.setOnClickListener {
            navigatePopBackStack()
        }
    }
}