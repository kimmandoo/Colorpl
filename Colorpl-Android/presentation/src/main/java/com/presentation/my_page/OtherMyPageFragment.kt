package com.presentation.my_page

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentOtherMyPageBinding
import com.presentation.base.BaseFragment
import com.presentation.util.setImageCircleCrop
import com.presentation.viewmodel.MyPageViewModel
import com.presentation.viewmodel.OtherMyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OtherMyPageFragment :
    BaseFragment<FragmentOtherMyPageBinding>(R.layout.fragment_other_my_page) {
    private val myPageViewModel: MyPageViewModel by viewModels()
    private val otherMyPageViewModel: OtherMyPageViewModel by viewModels()

    override fun initView() {
        initData()
        observeUiState()
    }


    private fun initData() {
        val safeArgs: OtherMyPageFragmentArgs by navArgs()
        myPageViewModel.getMemberInfo(true, safeArgs.memberInfo.memberId)
    }


    private fun observeUiState() {
        myPageViewModel.memberUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.member = it.memberInfo
                binding.ivProfileImg.setImageCircleCrop(it.memberInfo?.profileImage, true)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

}