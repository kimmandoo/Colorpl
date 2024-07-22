package com.presentation.sign

import android.content.Intent
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentLogInBinding
import com.presentation.MainActivity
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLogInBinding>(R.layout.fragment_log_in) {

    override fun initView() {
        clickLogin()
    }

    private fun clickLogin(){ //main으로 이동
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}