package com.presentation.my_page

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentProfileUpdateBinding
import com.presentation.base.BaseFragment
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.getPhotoGallery
import com.presentation.util.imeOptionsActionCheck
import com.presentation.util.setImage
import com.presentation.viewmodel.ProfileUpdateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@AndroidEntryPoint
class ProfileUpdateFragment :
    BaseFragment<FragmentProfileUpdateBinding>(R.layout.fragment_profile_update) {

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    private val profileUpdateViewModel: ProfileUpdateViewModel by viewModels()
    override fun initView() {
        initData()
        initClickEvent()
        observeNickName()
        observeProfileImage()
        observeUpdateSuccess()
    }

    private fun initData() {
        val safeArgs: ProfileUpdateFragmentArgs by navArgs()
        val data = safeArgs.member
        binding.apply {
            tiEtNickName.setText(data.nickName.toString())
            ivProfileImg.setImage(data.profileImage.toString())
        }
    }

    private fun observeNickName() {
        //이 부분은 수정 해야함
        with(binding.tiEtNickName) {
            addTextChangedListener {
                if (it?.isNotEmpty() == true) {
                    profileUpdateViewModel.setUpdateNickName(it.toString())
                    binding.tvComplete.isSelected = true
                } else {
                    binding.tilNickName.error = getString(R.string.my_page_nick_name_error)
                }
            }
            imeOptionsActionCheck {
                clearFocus()
            }
        }

    }

    private fun observeProfileImage() { //프로필 이미지
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.data?.let { uri ->
                        Timber.d("uri 확인 $uri")
                        binding.ivProfileImg.setImage(uri)
                        binding.tvComplete.isSelected = true
                        profileUpdateViewModel.setUpdateProfileImageFile(
                            ImageProcessingUtil(
                                requireActivity()
                            ).uriToFile(uri)
                        )

                    }
                }
            }
    }

    private fun observeUpdateSuccess() {
        profileUpdateViewModel.updateSuccess.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it) {
                    navigatePopBackStack()
                } else {
                    Toast.makeText(requireActivity(), "프로필 수정 실패!", Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initClickEvent() {
        binding.ivBack.setOnClickListener {
            navigatePopBackStack()
        }

        binding.tvComplete.setOnClickListener {
            profileUpdateViewModel.updateMemberInfo()
        }

        binding.ivProfileImg.setOnClickListener {
            getPhotoGallery(pickImageLauncher)
        }
    }

}