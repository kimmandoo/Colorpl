package com.presentation.reservation

import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationSeatBinding
import com.domain.model.Seat
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.reservation.SeatAdapter
import com.presentation.component.dialog.SeatDialog
import com.presentation.util.SeatClass
import com.presentation.util.ViewPagerManager
import com.presentation.util.formatWithCommas
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class ReservationSeatFragment :
    BaseFragment<FragmentReservationSeatBinding>(R.layout.fragment_reservation_seat) {
    private val viewModel: ReservationViewModel by viewModels({ requireParentFragment() })
    private val seatAdapter by lazy {
        SeatAdapter(onSeatSelected = { seat ->
            updateSeatSelection(seat)
        })
    }
    private var peopleCount = 1
    private val selectedSeats = mutableListOf<Seat>()

    override fun initView() {
        binding.apply {
            showPeopleCountBottomSheet()
            initUi()
//            viewModel.getReservationSeat(2, 109)
        }
    }

    private fun initUi() {
        binding.apply {
            rvSeats.apply {
                adapter = seatAdapter
                itemAnimator = null
                layoutManager = GridLayoutManager(requireContext(), COL_SIZE)
            }

            tvPersonChange.setOnClickListener {
                showPeopleCountBottomSheet()
            }
            tvNext.setOnClickListener {
                viewModel.setReservationSelectedSeat(selectedSeats.toMutableList())
                Timber.tag("selectedSeats").d(viewModel.reservationSelectedSeat.value.toString())
                ViewPagerManager.moveNext()
            }
        }

        binding.apply {
            tvGradeR.apply {
                this.tvGrade.text = SeatClass.R.value
                this.tvGrade.setBackgroundResource(R.drawable.rectangle_imperial_red_8_stroke_4_imperial_red)
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, 0)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, 0.formatWithCommas())
            }

            tvGradeS.apply {
                this.tvGrade.text = SeatClass.S.value
                this.tvGrade.setBackgroundResource(R.drawable.rectangle_green_8_stroke_4_green)
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, 0)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, 0.formatWithCommas())
            }

            tvGradeA.apply {
                this.tvGrade.text = SeatClass.A.value
                this.tvGrade.setBackgroundResource(R.drawable.rectangle_blue_8_stroke_4_blue)
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, 0)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, 0.formatWithCommas())
            }

            tvGradeB.apply {
                this.tvGrade.text = SeatClass.B.value
                this.tvGrade.setBackgroundResource(R.drawable.rectangle_purple_8_stroke_4_purple)
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, 0)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, 0.formatWithCommas())
            }
        }

    }

    private fun showPeopleCountBottomSheet() {
        val dialog = SeatDialog(requireContext(), peopleCount) { cnt ->
            bottomSheetClickListener(cnt)
        }
        dialog.show()
    }

//    private fun initViewModel() {
//        binding.apply {
//            this@apply.viewModel = viewModel
//        }
//    }

    private fun bottomSheetClickListener(count: Int) {
        peopleCount = count
        selectedSeats.clear()
        viewModel.clearReservationSeat()
        updateUiOfChangedPeopleCount()
//        seatAdapter.submitList(createSeats(ROW_SIZE, COL_SIZE))
        observeReservationSeat()
        observeReservationTotalPrice()
        findCenterPosition()
//        binding.tvPrice.text =
//            getString(R.string.reservation_seat_price, selectedSeats.sumOf { it.price })
    }

//    private fun createSeats(rowSize: Int, colSize: Int): List<Seat> {
//        return List(rowSize * colSize) { index ->
//            Seat(
//                row = index / colSize,
//                col = index % colSize,
//                grade = (1..5).random(),
//                isSelected = false
//            )
//        }
//    }

    private fun updateSeatSelection(seat: Seat) {
        val currentList = seatAdapter.currentList.toMutableList()
        val index = currentList.indexOfFirst { it.row == seat.row && it.col == seat.col }
        if (index != -1) {
            if (selectedSeats.size < peopleCount) {
                val updatedSeat = seat.copy(isSelected = !seat.isSelected)
                currentList[index] = updatedSeat
                if (updatedSeat.isSelected) {
                    selectedSeats.add(updatedSeat)
                } else {
                    selectedSeats.removeAll { it.row == updatedSeat.row && it.col == updatedSeat.col }
                }
            } else {
                if (seat.isSelected) {
                    val updatedSeat = seat.copy(isSelected = false)
                    currentList[index] = updatedSeat
                    selectedSeats.removeAll { it.row == updatedSeat.row && it.col == updatedSeat.col }
                }
            }
            viewModel.setReservationSeat(currentList)
            updateUiOfChangedPeopleCount()
        }

    }

    private fun updateUiOfChangedPeopleCount() {
        val gradeCount = selectedSeats.groupingBy { it.grade }.eachCount()
        binding.apply {
            tvGradeR.apply {
                val rCount = gradeCount[SeatClass.R.value] ?: 0
                val rPrice = viewModel.reservationPriceBySeatClass.value[SeatClass.R.value] ?: 0
                val totalPrice = (rCount * rPrice).formatWithCommas()
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, rCount)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, totalPrice)
            }
            tvGradeS.apply {
                val sCount = gradeCount[SeatClass.S.value] ?: 0
                val sPrice = viewModel.reservationPriceBySeatClass.value[SeatClass.S.value] ?: 0
                val totalPrice = (sCount * sPrice).formatWithCommas()
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, sCount)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, totalPrice)
            }
            tvGradeA.apply {
                val aCount = gradeCount[SeatClass.A.value] ?: 0
                val aPrice = viewModel.reservationPriceBySeatClass.value[SeatClass.A.value] ?: 0
                val totalPrice = (aCount * aPrice).formatWithCommas()
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, aCount)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, totalPrice)
            }
            tvGradeB.apply {
                val bCount = gradeCount[SeatClass.B.value] ?: 0
                val bPrice = viewModel.reservationPriceBySeatClass.value[SeatClass.B.value] ?: 0
                val totalPrice = (bCount * bPrice).formatWithCommas()
                this.tvGradeCnt.text = getString(R.string.reservation_seat_cnt, bCount)
                this.tvGradePrice.text = getString(R.string.reservation_seat_price, totalPrice)
            }
        }
//            binding.tvPrice.text =
//                getString(R.string.reservation_seat_price, selectedSeats.sumOf { it.price })

        updateConfirmState()
    }



    private fun updateConfirmState() {
        binding.tvNext.isSelected = if (selectedSeats.size == peopleCount) {
            binding.tvNext.text = "선택완료"
            true
        } else {
            binding.tvNext.text = "선택(${selectedSeats.size}/$peopleCount)"
            false
        }
    }

    private fun findCenterPosition() {
        binding.hsSeat.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.hsSeat.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val totalWidth = binding.rvSeats.width
                val visibleWidth = binding.hsSeat.width
                binding.hsSeat.scrollTo((totalWidth - visibleWidth) / 2, 0)
            }
        })
    }

    private fun observeReservationSeat() {
        viewModel.reservationSeat.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { reservationSeat ->
            seatAdapter.submitList(reservationSeat)
            Timber.tag("observeReservationSeat").d("$reservationSeat")
            viewModel.calculateTotalPrice()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeReservationTotalPrice() {
        viewModel.reservationTotalPrice.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { totalPrice ->
            Timber.tag("totalPrice").d("$totalPrice")
            binding.tvPrice.text = getString(R.string.reservation_seat_price, totalPrice.formatWithCommas())
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        const val COL_SIZE = 16
        const val ROW_SIZE = 10
    }
}