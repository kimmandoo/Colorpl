package com.presentation.my_page

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentProfileUpdateBinding
import com.presentation.base.BaseFragment
import com.presentation.util.getPhotoGallery
import com.presentation.util.imeOptionsActionCheck
import com.presentation.util.setImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileUpdateFragment :
    BaseFragment<FragmentProfileUpdateBinding>(R.layout.fragment_profile_update) {

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun initView() {
        initClickEvent()
        observeNickName()
        observeProfileImage()
    }

    private fun observeNickName() {
        //이 부분은 수정 해야함
        with(binding.tiEtNickName) {
            addTextChangedListener {
                if (it?.isNotEmpty() == true) {
                    binding.tilNickName.error = "워닝워닝워닝"
                    binding.tvComplete.isSelected = true
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
                        binding.ivProfileImg.setImage(uri)
                    }
                }
            }
    }

    private fun initClickEvent() {
        binding.ivBack.setOnClickListener {
            navigatePopBackStack(findNavController())
        }

        binding.tvComplete.setOnClickListener {
            navigatePopBackStack(findNavController())
        }

        binding.ivProfileImg.setOnClickListener {
            getPhotoGallery(pickImageLauncher)
        }
    }

}