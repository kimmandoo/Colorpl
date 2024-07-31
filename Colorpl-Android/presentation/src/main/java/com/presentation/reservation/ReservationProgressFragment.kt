package com.presentation.reservation


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationProgressBinding
import com.presentation.base.BaseFragment
import com.presentation.util.TopButtonsStatus
import com.presentation.util.ViewPagerManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReservationProgressFragment : BaseFragment<FragmentReservationProgressBinding>(R.layout.fragment_reservation_progress) {
    override fun initView() {
        initUi()
    }

    private fun initUi() {
        initViewPager()
        initWormDotsIndicator()
        binding.apply {
            type = TopButtonsStatus.BOTH
            // ViewPagerManager에 ViewPager 인스턴스 설정
            ViewPagerManager.setViewPager(this@apply.vpScreen)
        }
    }

    /** ViewPager 설정 */
    private fun initViewPager() {
        binding.vpScreen.apply {
            this@apply.adapter = ViewPagerAdapter(this@ReservationProgressFragment)
            this@apply.isUserInputEnabled = false
        }

    }
    /** WormDotsIndicator 설정 */
    private fun initWormDotsIndicator() {
        binding.wdiProgress.apply {
            this@apply.setViewPager2(binding.vpScreen)
            this@apply.dotsClickable = true // 추후 false 처리.
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
                2 -> ReservationInfoFragment()
                3 -> ReservationCompleteFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }


}