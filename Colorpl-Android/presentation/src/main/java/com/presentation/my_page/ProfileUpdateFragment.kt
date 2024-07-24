package com.presentation.my_page

import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentProfileUpdateBinding
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileUpdateFragment : BaseFragment<FragmentProfileUpdateBinding>(R.layout.fragment_profile_update) {

    override fun initView() {
        initClickEvent()
        observeNickName()
    }

    private fun observeNickName(){
        binding.tiEtNickName.addTextChangedListener {
            if(it?.isNotEmpty() == true){
                binding.tilNickName.error = "워닝워닝워닝"
            }
        }
    }

    private fun initClickEvent(){
        binding.ivBack.setOnClickListener {
            navigatePopBackStack(findNavController())
        }
    }

}