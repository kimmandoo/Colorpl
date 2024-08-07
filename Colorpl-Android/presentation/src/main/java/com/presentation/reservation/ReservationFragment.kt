package com.presentation.reservation

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationBinding
import com.domain.model.FilterItem
import com.domain.model.ReservationInfo
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.feed.FilterAdapter
import com.presentation.component.adapter.reservation.ReservationInfoAdapter
import com.presentation.component.dialog.DateRangePickerDialog
import com.presentation.component.dialog.LocationPickerDialog
import com.presentation.util.getFilterItems
import com.presentation.viewmodel.ReservationListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ReservationFragment :
    BaseFragment<FragmentReservationBinding>(R.layout.fragment_reservation) {

    @Inject
    @Named("bootpay")
    lateinit var applicationId: String
    private val reservationListViewModel: ReservationListViewModel by viewModels()

    private val filterAdapter by lazy {
        FilterAdapter(onItemClickListener = { filterItem ->
            onFilterClickListener(filterItem)
        })
    }

    private val reservationInfoAdapter by lazy {
        ReservationInfoAdapter(
            onClickListener = { reservationInfo ->
                onClickListener(reservationInfo)
            },
        )
    }

    override fun initView() {
        initFilter()
        initReservationInfo()
        initClickListener()
        initReservationList()
        observeReservationList()
    }

    private fun initReservationList() {
        reservationListViewModel.getReservationList()
    }

    private fun observeReservationList() {
        reservationListViewModel.reservationList.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { reservationList ->
            reservationInfoAdapter.submitList(reservationList)
            Timber.tag("reservationList").d(reservationList.toString())
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initFilter() {
        binding.rvFilter.apply {
            adapter = filterAdapter
            itemAnimator = null
        }
        filterAdapter.submitList(binding.root.context.getFilterItems())
    }

    private fun initReservationInfo() {
        binding.rvReservationInfo.apply {
            adapter = reservationInfoAdapter
            itemAnimator = null
        }
        val testReservationInfo = mutableListOf<ReservationInfo>()
        repeat(10) {
            testReservationInfo.add(
                ReservationInfo(
                    reservationInfoId = 2,
                    contentImg = null,
                    title = "님과함께 : 테스트",
                    category = "사극",
                    runtime = "3시간 00분",
                    price = "13,000"
                )
            )
        }
//        reservationInfoAdapter.submitList(testReservationInfo)
    }

    private fun initClickListener() {
        binding.apply {
            clSelectDate.setOnClickListener {
                showDateRangePickerDialog()
            }
            clSelectLocation.setOnClickListener {
                showLocationPickerDialog()
            }
            ivFilter.setOnClickListener {
                initReservationList()
            }
        }
    }

    private fun onFilterClickListener(clickedItem: FilterItem) {
        val updatedList = filterAdapter.currentList.map { item ->
            if (item.name == clickedItem.name) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }

        filterAdapter.submitList(updatedList)
    }

    private fun onClickListener(data: ReservationInfo) {
        val action =
            ReservationFragmentDirections.actionFragmentReservationToFragmentReservationDetail(data)
        Navigation.findNavController(binding.root).navigate(action)
    }

    /** 날짜 선택 캘린더 Dialog */
    private fun showDateRangePickerDialog() {
        Toast.makeText(binding.root.context, "날짜 클릭", Toast.LENGTH_SHORT).show()
        val dateRangePickerDialog = DateRangePickerDialog(requireContext()) { year, month, day ->
            // 날짜 범위를 선택한 후 수행할 작업을 여기에 추가합니다.
            Toast.makeText(
                binding.root.context,
                "년: $year, 월: ${month + 1}, 일: $day",
                Toast.LENGTH_SHORT
            ).show()
            binding.tvSelectDate.text = "$year.${month + 1}.$day"
        }
        dateRangePickerDialog.show()
    }

    /** 지역 선택 리스트 Dialog */
    private fun showLocationPickerDialog() {
        Toast.makeText(binding.root.context, "날짜 클릭", Toast.LENGTH_SHORT).show()
        val locationList = arrayOf("전국", "서울", "경기·인천", "강원", "충청·대전·세종", "경상·대구·울산·부산", "전라·광주")
        val locationPickerDialog =
            LocationPickerDialog(requireContext(), locationList) { selectedCity ->
                binding.tvSelectLocation.text = selectedCity
            }
        locationPickerDialog.show()
    }


    /**
     * 결제할 아이템 하나에 대한 return
     * 이걸 각 아이템별로 호출해서 List로 만들어 결제요청
     * @param itemCode
     * @param itemName
     * @param itemPrice
     * @param itemQuantity
     */
    fun selectItemToPay(
        itemName: String,
        itemCode: String,
        itemQuantity: Int,
        itemPrice: Double
    ): BootItem {
        return BootItem().setName(itemName).setId(itemCode).setQty(itemQuantity).setPrice(itemPrice)
    }

    /**
     *  일시불만 가능하게 함
     *  @param itemList : selectItemToPay에서 가져온 아이템들을 리스트로 묶어서 넣어줘야됨
     */
    fun requestPayment(
        v: View?,
        user: BootUser,
        itemList: MutableList<BootItem>,
        orderName: String,
        orderId: String
    ) {
        val payload = Payload()
        var totalPrice: Double = 0.0
        itemList.forEachIndexed { index, bootItem ->
            totalPrice += bootItem.price
        }
        payload.setApplicationId(applicationId)
            .setOrderName(orderName)
            .setOrderId(orderId)
            .setPrice(totalPrice)
            .setUser(user)

        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map
        Bootpay.init(requireActivity().supportFragmentManager, requireContext())
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Timber.tag("bootpay").d("cancel: $data")
                }

                override fun onError(data: String) {
                    Timber.tag("bootpay").d("error: $data")
                }

                override fun onClose() {
                    Bootpay.removePaymentWindow()
                }

                override fun onIssued(data: String) {
                    Timber.tag("bootpay").d("issued: $data")
                }

                override fun onConfirm(data: String): Boolean {
                    if (checkClientValidation(data)) {
                        // 재고가 있으면 승인
                        Bootpay.transactionConfirm(data)
                        return false;
                    } else {
                        Bootpay.dismissWindow()
                        return false;
                    }
                }

                override fun onDone(data: String) {
                    Timber.tag("bootpay").d("done: $data")
                }
            }).requestPayment()
    }

    /**
     * 서버에 요청해서 재곻 확인할 함수
     */
    private fun checkClientValidation(data: String): Boolean {

        return false
    }

    private fun getBootUser(
        userId: String,
        email: String,
        phone: String,
        username: String,
    ): BootUser {
        val user = BootUser()
        user.id = userId
        user.email = email
        user.phone = phone
        user.username = username
        return user
    }
}