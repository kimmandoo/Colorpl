package com.presentation.reservation

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationPaymentBinding
import com.presentation.base.BaseFragment
import com.presentation.util.Payment
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReservationPaymentFragment : BaseFragment<FragmentReservationPaymentBinding>(R.layout.fragment_reservation_payment) {
    private val viewModel: ReservationViewModel by viewModels({ requireParentFragment() })

    private var paymentDiscount: Payment.Discount = Payment.Discount.NONE
    private var paymentMethod: Payment.Method = Payment.Method.NONE

    override fun initView() {
        initUi()
    }

    private fun initUi() {
        initPayment()
        binding.viewModel = viewModel
    }

    /** 할인적용 및 결제방법 */
    private fun initPayment() {
        binding.apply {
            tvSsafyDiscount.setOnClickListener {
                tvSsafyDiscount.isSelected = !tvSsafyDiscount.isSelected
            }
            tvCouponDiscount.setOnClickListener {
                tvCouponDiscount.isSelected = !tvCouponDiscount.isSelected
            }

            tvBootPay.setOnClickListener {
                handlePaymentMethodSelection(tvBootPay.id)
            }
            tvSsafyPay.setOnClickListener {
                handlePaymentMethodSelection(tvSsafyPay.id)
            }
            tvPayNext.setOnClickListener {
                Toast.makeText(requireContext(), "결제를 해봅시다~", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun handlePaymentMethodSelection(selectedPaymentMethodId: Int) {
        // 모든 결제 방법 선택 해제
        binding.apply {
            tvBootPay.isSelected = false
            tvSsafyPay.isSelected = false


            // 선택된 결제 방법만 선택
            when (selectedPaymentMethodId) {
                tvBootPay.id -> {
                    this@ReservationPaymentFragment.paymentMethod = Payment.Method.BOOT
                    tvBootPay.isSelected = true
                }
                tvSsafyPay.id -> {
                    this@ReservationPaymentFragment.paymentMethod = Payment.Method.SSAFY
                    tvSsafyPay.isSelected = true
                }

            }
        }
        Timber.d("$paymentMethod")
        updateConfirmState()
    }

    private fun updateConfirmState() {
        binding.tvPayNext.isSelected = paymentMethod != Payment.Method.NONE
        Timber.d("${paymentMethod != Payment.Method.NONE}")
    }
}
