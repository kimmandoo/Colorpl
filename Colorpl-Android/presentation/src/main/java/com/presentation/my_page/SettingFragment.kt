package com.presentation.my_page

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSettingBinding
import com.presentation.base.BaseFragment
import com.presentation.sign.SignActivity
import com.presentation.viewmodel.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val settingViewModel: SettingViewModel by viewModels()
    override fun initView() {
        initClickEvent()
        observeLogOut()
    }

    private fun observeLogOut(){
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                settingViewModel.signOutEvent.collectLatest{
                    if(it == 1){
                        startActivity(Intent(requireContext(), SignActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }
    }


    private fun initClickEvent() {
        binding.apply {
            tvLogout.setOnClickListener {
                settingViewModel.signOut()
            }
        }
    }
}