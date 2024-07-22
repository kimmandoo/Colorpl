package com.presentation.sign

import android.view.View
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpBinding
import com.presentation.base.BaseFragment
import com.presentation.util.imeOptionsActionCheck
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    override fun initView() {
        observeImeOptions()

    }

    private fun observeImeOptions() {
        binding.apply {
            includeId.etContent.imeOptionsActionCheck(requireActivity()) {
                clPassword.visibility = View.VISIBLE
            }
            includePassword.etContent.imeOptionsActionCheck(requireActivity()) {
                clNickname.visibility = View.VISIBLE
            }
            includeNickname.etContent.imeOptionsActionCheck(requireActivity()) {
                clProfileImage.visibility = View.VISIBLE
            }
        }
    }

}