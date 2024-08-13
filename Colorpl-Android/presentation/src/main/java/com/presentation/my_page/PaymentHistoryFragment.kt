package com.presentation.my_page

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentPaymentHistoryBinding
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.mypage.PaymentHistoryAdapter
import com.presentation.component.custom.CustomDialog
import com.presentation.component.custom.showCustomDropDownMenu
import com.presentation.util.DropDownMenu
import com.presentation.util.PaymentResult
import com.presentation.viewmodel.PayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class PaymentHistoryFragment :
    BaseFragment<FragmentPaymentHistoryBinding>(R.layout.fragment_payment_history) {

    private val payViewModel: PayViewModel by viewModels()


    val paymentHistoryAdapter: PaymentHistoryAdapter by lazy {
        PaymentHistoryAdapter()
    }

    override fun initView() {
        initAdapter()
        initClickEvent()
        observePaymentToken()
        observePaymentReceipt()
    }

    private fun initAdapter() {
        binding.rcPaymentHistory.apply {
            adapter = paymentHistoryAdapter
            itemAnimator = null
        }


    }

    private fun observePaymentToken() {
        payViewModel.payToken.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                payViewModel.getPaymentReceipts()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observePaymentReceipt() {
        payViewModel.paymentReceipts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                dismissLoading()
                binding.emptyVisible = it.isEmpty()
                paymentHistoryAdapter.submitList(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun initClickEvent() {
        binding.includeTop.clBack.setOnClickListener {
            navigatePopBackStack()
        }


        paymentHistoryAdapter.setItemClickListener { view, payResult, data ->
            showCustomDropDownMenu(
                requireActivity(),
                view,
                DropDownMenu.getDropDown(PaymentResult.getMenu(payResult), requireActivity()),
                action = { value ->
                    Timber.d("receiptId 확인 ${data.receiptId}")
                    val title = when (value.type) {
                        DropDownMenu.DELETE -> {
                            requireActivity().getString(R.string.my_page_pay_history_delete_dialog_title)
                        }

                        DropDownMenu.REFUND -> {
                            requireActivity().getString(R.string.my_page_cancel_dialog_title)
                        }
                    }
                    initDialog(
                        data.receiptId, title,
                        value.type
                    )
                }
            )
        }
    }

    private fun initDialog(receiptId: String, title: String, type: DropDownMenu) {
        CustomDialog(requireActivity()).cancellableDialog(title,
            complete = {
                showLoading()
                if (type == DropDownMenu.REFUND) {
                    payViewModel.payCancel(receiptId)
                } else {
                    payViewModel.payHistoryDelete(receiptId)
                }

            },
            cancel = {})
    }
}