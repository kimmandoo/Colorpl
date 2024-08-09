package com.presentation.reservation

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationPaymentBinding
import com.domain.model.PayRequest
import com.google.gson.Gson
import com.presentation.base.BaseFragment
import com.presentation.reservation.model.PaymentEventState
import com.presentation.util.Payment
import com.presentation.util.ViewPagerManager
import com.presentation.util.getBootUser
import com.presentation.util.requestPayment
import com.presentation.util.selectItemToPay
import com.presentation.viewmodel.PayViewModel
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.bootpay.android.Bootpay
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ReservationPaymentFragment :
    BaseFragment<FragmentReservationPaymentBinding>(R.layout.fragment_reservation_payment) {
    private val viewModel: ReservationViewModel by viewModels({ requireParentFragment() })
    private val payViewModel: PayViewModel by viewModels()

    @Inject
    @Named("bootpay")
    lateinit var applicationId: String

    private var paymentDiscount: Payment.Discount = Payment.Discount.NONE
    private var paymentMethod: Payment.Method = Payment.Method.NONE

    override fun initView() {
        initUi()
        observePaymentResult()
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
                val bootUser = getBootUser("", "", "", "")
                val bootItem = mutableListOf(selectItemToPay("", "", 1, 100.0))
                requestPayment(
                    v = null,
                    applicationId = applicationId,
                    user = bootUser,
                    itemList = bootItem,
                    orderName = "주문하세요",
                    orderId = "123",
                    context = requireActivity(),
                    manager = requireActivity().supportFragmentManager,
                ) { data ->
                    Timber.d("영수증 id 받아오기 $data")
//                    val responseData = Gson().fromJson(data, PayRequest::class.java)
//                    payViewModel.startPayment(responseData.receipt_id)
                    false
                }
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

    private fun observePaymentResult() {
        payViewModel.paymentEventState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is PaymentEventState.PaySuccess -> {
                        ViewPagerManager.moveNext()
                    }

                    is PaymentEventState.PayFail -> {
                        Toast.makeText(requireActivity(), "결제 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateConfirmState() {
        binding.tvPayNext.isSelected = paymentMethod != Payment.Method.NONE
        Timber.d("${paymentMethod != Payment.Method.NONE}")
    }
}
