package com.presentation.my_page

import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentProfileUpdateBinding
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileUpdateFragment : BaseFragment<FragmentProfileUpdateBinding>(R.layout.fragment_profile_update) {

    override fun initView() {
        initClickEvent()
    }

    private fun initClickEvent(){
        binding.ivBack.setOnClickListener {
            navigatePopBackStack(findNavController())
        }
    }

}