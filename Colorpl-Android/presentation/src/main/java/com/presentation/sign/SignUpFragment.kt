package com.presentation.sign

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpBinding
import com.presentation.base.BaseFragment
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.Sign
import com.presentation.util.emailCheck
import com.presentation.util.getPhotoGallery
import com.presentation.util.hideKeyboard
import com.presentation.util.imeOptionsActionCheck
import com.presentation.util.onBackButtonPressed
import com.presentation.util.setImage
import com.presentation.util.setPasswordTransformation
import com.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.sign_nav_graph)

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>


    override fun initView() {
        initSetting()
        setImeOptions()
        observeSignType()
        observeProfileImage()
        observeEditText()
        observeNextButton()
        initClickEvent()
        backEvent()
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


    private fun observeEditText() { // 텍스트 삽입
        binding.includeId.etContent.addTextChangedListener {
            signUpViewModel.setUserEmail(it.toString())
            binding.includeId.apply {
                if (it?.isNotEmpty() == true) {
                    tvError.visibility = View.VISIBLE
                    errorCheck = it.toString().emailCheck()
                    tvError.isSelected = !it.toString().emailCheck()
                }
            }
        }

        binding.includePassword.etContent.addTextChangedListener {
            signUpViewModel.setPassWord(it.toString())
        }

        binding.includeNickname.etContent.addTextChangedListener {
            signUpViewModel.setUserNickName(it.toString())
        }
    }

    private fun observeNextButton() { //다음 버튼 활성화
        signUpViewModel.nextButton.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.tvNext.apply {
                    isSelected = it
                    isEnabled = it
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeSignType() { //Type 감지
        signUpViewModel.typeEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.apply {
                    type = it
                    when (it) {
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

                        else -> {

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
                        signUpViewModel.setUserImageFile(ImageProcessingUtil(requireActivity()).uriToFile(uri))
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
            signUpViewModel.clearData()
            navigatePopBackStack()
        }

        binding.tvNext.setOnClickListener {
            navigateDestination(
                R.id.action_fragment_sign_up_to_fragment_sign_up_preference
            )
        }
    }

    private fun backEvent() {
        requireActivity().onBackButtonPressed(viewLifecycleOwner) {
            signUpViewModel.clearData()
            navigatePopBackStack()
        }
    }

}

