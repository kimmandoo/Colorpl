package com.presentation.sign

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpBinding
import com.presentation.base.BaseFragment
import com.presentation.util.Sign
import com.presentation.util.getPhotoGallery
import com.presentation.util.hideKeyboard
import com.presentation.util.imeOptionsActionCheck
import com.presentation.util.setImage
import com.presentation.util.setPasswordTransformation
import com.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel: SignUpViewModel by viewModels()

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun initView() {
        initSetting()
        setImeOptions()
        observeSignType()
        observeProfileImage()
        initClickEvent()
    }

    private fun initSetting() { //초기 세팅
        binding.includePassword.etContent.transformationMethod =
            PasswordTransformationMethod.getInstance()
        binding.tvNext.isSelected = true // -> 얘는 로직 짤때 변경할거임
    }

    private fun setImeOptions() {
        binding.apply {
            includeId.etContent.imeOptionsActionCheck {
                signUpViewModel.setTypeEvent(Sign.PASSWORD)
            }
            includePassword.etContent.imeOptionsActionCheck {
                signUpViewModel.setTypeEvent(Sign.NICKNAME)
            }
            includeNickname.etContent.imeOptionsActionCheck {
                signUpViewModel.setTypeEvent(Sign.PROFILE)
            }
        }
    }

    private fun observeSignType() { //Type 감지
        signUpViewModel.typeEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.apply {
                    type = it
                    when (it) {
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
                            includeNickname.etContent.imeOptions = EditorInfo.IME_ACTION_DONE
                        }

                        Sign.PROFILE -> {
                            includeNickname.titleVisible = true
                            includeNickname.etContent.clearFocus()
                            clProfileImage.visibility = View.VISIBLE
                            requireActivity().hideKeyboard(clProfileImage)
                        }
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProfileImage() { //프로필 이미지
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.data?.let { uri ->
                        binding.ivProfile.setImage(uri)
                    }
                }
            }
    }


    private fun initClickEvent() {
        binding.includePassword.ivPasswordToggle.setOnClickListener { // password Toggle
            binding.includePassword.etContent.setPasswordTransformation()
        }

        binding.ivProfile.setOnClickListener { //프로필 이미지 클릭
            getPhotoGallery(pickImageLauncher)
        }

        binding.ivBack.setOnClickListener { //뒤로 가기
            navigatePopBackStack()
        }

        binding.tvNext.setOnClickListener {
            navigateDestination(
                R.id.action_fragment_sign_up_to_fragment_sign_up_preference
            )
        }
    }
}

