package com.presentation.reservation


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentReservationProgressBinding
import com.presentation.base.BaseFragment
import com.presentation.util.TopButtonsStatus
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

        // WormDotsIndicator와 ViewPager 연결
        val wormDotsIndicator = binding.wdiProgress
        wormDotsIndicator.setViewPager2(viewPager)
    }

    private inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 3 // 페이지 수 (예: 3개의 페이지)
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ReservationFragment()
                1 -> ReservationFragment()
                2 -> ReservationFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }


}