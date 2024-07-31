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
        binding.type = TopButtonsStatus.BOTH

        // ViewPager 설정
        val viewPager = binding.vpScreen
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.isUserInputEnabled = false

        // WormDotsIndicator와 ViewPager 연결
        val wormDotsIndicator = binding.wdiProgress
        wormDotsIndicator.setViewPager2(viewPager)
        wormDotsIndicator.type
        wormDotsIndicator.dotsClickable = true

        // ViewPagerManager에 ViewPager 인스턴스 설정
        ViewPagerManager.setViewPager(viewPager)
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