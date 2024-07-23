package com.presentation.sign

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpBinding
import com.presentation.base.BaseFragment
import com.presentation.util.Sign
import com.presentation.util.imeOptionsActionCheck
import com.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel : SignUpViewModel by viewModels()

    override fun initView() {
        setImeOptions()
        observeSignType()
    }

    private fun setImeOptions() {
        binding.apply {
            includeId.etContent.imeOptionsActionCheck {
                signUpViewModel.setTypeEvent(Sign.PASSWORD)
            }
            includePassword.etContent.imeOptionsActionCheck {
                signUpViewModel.setTypeEvent(Sign.NICKNAME)
            }
            includeNickname.etContent.imeOptionsActionCheck{
                signUpViewModel.setTypeEvent(Sign.PROFILE)
            }
        }
    }

    private fun observeSignType(){ //Type 감지ㅁㄴ
        signUpViewModel.typeEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.apply {
                    type = it
                    when(it){
                        Sign.ID -> {

                        }
                        Sign.PASSWORD -> {
                            includeId.etContent.clearFocus()
                            includeId.titleVisible = true
                            includePassword.etContent.requestFocus()
                            clPassword.visibility = View.VISIBLE
                        }
                        Sign.NICKNAME -> {
                            includePassword.etContent.clearFocus()
                            includePassword.titleVisible = true
                            includeNickname.etContent.requestFocus()
                            clNickname.visibility = View.VISIBLE
                        }
                        Sign.PROFILE -> {
                            includeNickname.titleVisible = true
                            includeNickname.etContent.clearFocus()
                            clProfileImage.visibility = View.VISIBLE
                        }
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}