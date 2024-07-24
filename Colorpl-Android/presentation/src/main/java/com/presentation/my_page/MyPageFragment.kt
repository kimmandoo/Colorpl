package com.presentation.my_page

import android.animation.ObjectAnimator
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMyPageBinding
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun initView() {
        initClickEvent()
    }


    private fun initClickEvent(){
        val star = binding.ivTicketStar
        val expire = binding.ivTicketExpire
        star.isSelected = true
        binding.ivTicketStar.setOnClickListener {
            it.isSelected = !it.isSelected
            expire.isSelected = !it.isSelected
        }

        binding.ivTicketExpire.setOnClickListener {
            it.isSelected = !it.isSelected
            star.isSelected = !it.isSelected
        }
    }

}