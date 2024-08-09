package com.presentation.my_page

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentPaymentHistoryBinding
import com.domain.model.Payment
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.mypage.PaymentHistoryAdapter
import com.presentation.component.custom.showCustomDropDownMenu
import com.presentation.util.DropDownMenu
import com.presentation.util.PaymentResult
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        binding.includeTop.clBack.setOnClickListener {
            navigatePopBackStack()
        }


        paymentHistoryAdapter.setItemClickListener { view, paymentResult ->
            showCustomDropDownMenu(
                requireActivity(),
                view,
                DropDownMenu.getDropDown(PaymentResult.getMenu(paymentResult), requireActivity()),
                action = { value ->
                    Timber.d("데이터 확인 $value")
                }
            )
        }


    }
}