package com.presentation.sign

import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentLoginBinding
import com.presentation.MainActivity
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun initView() {
        initIncludeView()
        initClickEvent()
    }

    private fun initIncludeView() {
        binding.includePassword.apply {
            this.etContent.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }
    private fun initClickEvent(){
        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
            tvSignUp.setOnClickListener {
                navigateDestination(findNavController(), R.id.action_fragment_login_to_fragment_sign_up)
            }
        }
    }

}