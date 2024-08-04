package com.presentation.reservation


import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationProgressBinding
import com.presentation.base.BaseFragment
import com.presentation.util.TopButtonsStatus
import com.presentation.util.ViewPagerManager
import com.presentation.util.onBackButtonPressed
import com.presentation.viewmodel.ReservationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReservationProgressFragment : BaseFragment<FragmentReservationProgressBinding>(R.layout.fragment_reservation_progress) {
    private val args: ReservationProgressFragmentArgs by navArgs()
    private val viewModel: ReservationViewModel by viewModels()


    override fun initView() {
        initUi()
//        backEvent()
    }

    private fun initUi() {
        initViewPager()
        initWormDotsIndicator()
        initTopButtonsClickEvent()
        initViewModelData()
        Timber.d("${args.reservationDetail.title}")
    }

    private fun initViewModelData() {
        args.reservationDetail.contentImg?.let { viewModel.setReservationImg(it) }
        args.reservationDetail.title.let { viewModel.setReservationTitle(it) }
    }

    /** ViewPager 설정 */
    private fun initViewPager() {
        binding.vpScreen.apply {
            this@apply.adapter = ViewPagerAdapter(this@ReservationProgressFragment)
            this@apply.isUserInputEnabled = false
            // ViewPagerManager에 ViewPager 인스턴스 설정
            ViewPagerManager.setViewPager(this@apply)

            // OnPageChangeCallback 추가
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handlePageSelected(position)
                    handleBackEvent(position)
                }
            })
        }
    }

    /** 상단 버튼 visibility 유무.
     * @param position 페이지 */
    private fun handlePageSelected(position: Int) {
        val status = when (position) {
            0, 3 -> TopButtonsStatus.EXIT
            1, 2 -> TopButtonsStatus.BOTH
            else -> throw IllegalStateException("Unexpected position $position")
        }
        binding.type = status
    }

    /** 시스템 뒤로가기 이벤트 처리.
     * @param position 페이지 */
    private fun handleBackEvent(position: Int) {
        requireActivity().onBackButtonPressed(viewLifecycleOwner) {
            when (position) {
                0, 3 -> navigatePopBackStack()
                1, 2 -> ViewPagerManager.movePrevious()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

    /** WormDotsIndicator 설정 */
    private fun initWormDotsIndicator() {
        binding.wdiProgress.apply {
            this@apply.setViewPager2(binding.vpScreen)
            this@apply.dotsClickable = true // 추후 false 처리.
        }
    }

    /** 상단 버튼 클릭 이벤트 설정 */
    private fun initTopButtonsClickEvent() {
        binding.includeTopCenter.apply {
            ivBack.setOnClickListener{
                ViewPagerManager.movePrevious()
            }
            ivExit.setOnClickListener {
                navigatePopBackStack()
            }
        }
    }


    private inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 4
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ReservationTimeTableFragment()
                1 -> ReservationSeatFragment()
                2 -> ReservationPaymentFragment()
                3 -> ReservationCompleteFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }


}