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
import com.presentation.util.formatWithCommas
import com.presentation.util.getBootUser
import com.presentation.util.makeMetaData
import com.presentation.util.requestPayment
import com.presentation.util.selectItemToPay
import com.presentation.viewmodel.PayViewModel
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ReservationPaymentFragment :
    BaseFragment<FragmentReservationPaymentBinding>(R.layout.fragment_reservation_payment) {
    private val reservationViewModel: ReservationViewModel by viewModels({ requireParentFragment() })
    private val payViewModel: PayViewModel by viewModels()

    @Inject
    @Named("bootpay")
    lateinit var applicationId: String


    override fun initView() {
        initUi()
        observePaymentResult()
        observeReservationPayInfo()
        observeReservationDiscount()
    }

    private fun initUi() {
        binding.viewModel = reservationViewModel
        initPayment()
    }

    /** 할인적용 및 결제방법 */
    private fun initPayment() {
        binding.apply {
            tvSsafyDiscount.setOnClickListener {
                if (tvCouponDiscount.isSelected) {
                    tvCouponDiscount.isSelected = false
                }
                tvSsafyDiscount.isSelected = !tvSsafyDiscount.isSelected
                if (tvSsafyDiscount.isSelected) {
                    reservationViewModel.setReservationPayDiscount(Payment.Discount.SSAFY_TRAINEE)
                } else {
                    reservationViewModel.setReservationPayDiscount(Payment.Discount.NONE)
                }
            }
            tvCouponDiscount.setOnClickListener {
                if (tvSsafyDiscount.isSelected) {
                    tvSsafyDiscount.isSelected = false
                }
                tvCouponDiscount.isSelected = !tvCouponDiscount.isSelected
                if (tvCouponDiscount.isSelected) {
                    reservationViewModel.setReservationPayDiscount(Payment.Discount.COUPON)
                } else {
                    reservationViewModel.setReservationPayDiscount(Payment.Discount.NONE)
                }
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
                val selectedSeatList: List<Map<String, Any>> =
                    reservationViewModel.reservationSelectedSeat.value.map { it.convertToHashMap() }

                val metaDataMap: MutableMap<String, Any> = makeMetaData(
                    showName = reservationViewModel.reservationTitle.value,
                    showHallName = reservationViewModel.reservationPlace.value,
                    showTheaterName = reservationViewModel.reservationTheater.value,
                    showDetailId = reservationViewModel.reservationDetailId.value,
                    showScheduleId = reservationViewModel.reservationTimeTable.value.scheduleId,
                    selectedSeatList = selectedSeatList,
                    selectedDiscount = null
                )


                requestPayment(
                    v = null,
                    applicationId = applicationId,
                    user = bootUser,
                    itemList = bootItem,
                    orderName = reservationViewModel.reservationTitle.value,
                    orderId = "123",
                    context = requireActivity(),
                    manager = requireActivity().supportFragmentManager,
                    metaDataMap = metaDataMap
                ) { data ->
                    Timber.d("영수증 id 받아오기 $data")
                    val responseData = Gson().fromJson(data, PayRequest::class.java)
                    payViewModel.startPayment(responseData.receipt_id)
                    false
                }
            }
        }
    }

    /** 결제 방법 선택 */
    private fun handlePaymentMethodSelection(selectedPaymentMethodId: Int) {
        // 모든 결제 방법 선택 해제
        binding.apply {
            tvBootPay.isSelected = false
            tvSsafyPay.isSelected = false

            // 선택된 결제 방법만 선택
            when (selectedPaymentMethodId) {
                tvBootPay.id -> {
                    reservationViewModel.setReservationPayMethod(Payment.Method.BOOT)
                    tvBootPay.isSelected = true
                }

                tvSsafyPay.id -> {
                    reservationViewModel.setReservationPayMethod(Payment.Method.SSAFY)
                    tvSsafyPay.isSelected = true
                }
            }
        }
        updateConfirmState()
    }

    /** 결제 결과 observe */
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

    /** 가격 정보 observe */
    private fun observeReservationPayInfo() {
        reservationViewModel.reservationPayInfo.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { payInfo ->
                updateUiReservationPayInfo()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /** 할인 정책 observe.*/
    private fun observeReservationDiscount() {
        reservationViewModel.reservationPayDiscount.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { discount ->
                reservationViewModel.updatePayInfo()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /** 다음 버튼 update. */
    private fun updateConfirmState() {
        binding.tvPayNext.isSelected =
            reservationViewModel.reservationPayMethod.value != Payment.Method.NONE
    }

    /** 가격 정보 UI update.*/
    private fun updateUiReservationPayInfo() {
        binding.apply {
            tvPayValue.text = getString(
                R.string.reservation_price,
                reservationViewModel.reservationPayInfo.value.amountOfBefore.formatWithCommas()
            )
            tvDiscountPayValue.text = getString(
                R.string.reservation_price,
                reservationViewModel.reservationPayInfo.value.amountOfDiscount.formatWithCommas()
            )
            tvResultPayValue.text = getString(
                R.string.reservation_price,
                reservationViewModel.reservationPayInfo.value.amountOfAfter.formatWithCommas()
            )
        }
    }
}
