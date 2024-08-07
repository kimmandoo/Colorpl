package com.presentation.my_page

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSettingBinding
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    override fun initView() {

    }


    private fun initClickEvent(){
        binding.apply {
            tvLogout.setOnClickListener{

            }
        }
    }
}