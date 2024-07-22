package com.presentation.sign

import android.content.Intent
import android.view.inputmethod.EditorInfo
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentLoginBinding
import com.presentation.MainActivity
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun initView() {
        initIncludeView()
        clickLogin()
    }

    private fun initIncludeView() {
        binding.includePassword.apply {
            this.etContent.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    private fun clickLogin() { //main으로 이동
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}