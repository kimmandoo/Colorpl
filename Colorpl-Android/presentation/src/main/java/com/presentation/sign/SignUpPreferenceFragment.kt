package com.presentation.sign

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpPreferenceBinding
import com.presentation.MainActivity
import com.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpPreferenceFragment :
    BaseFragment<FragmentSignUpPreferenceBinding>(R.layout.fragment_sign_up_preference) {

    override fun initView() {
        initClickEvent()
    }


    private fun initClickEvent() {
        val item = listOf(
            binding.includeMovie,
            binding.includeCircus,
            binding.includeMusical,
            binding.includeTheatre,
            binding.includeExhibition
        )

        //취향 클릭
        item.forEach { view ->
            view.clPreference.setOnClickListener {
                val selected = !it.isSelected
                it.isSelected = selected
                view.ivIcon.isSelected = selected
                view.tvType.isSelected = selected
                view.isSelected = selected
            }
        }


        binding.ivBack.setOnClickListener {
            navigatePopBackStack(findNavController())
        }

        binding.tvNext.setOnClickListener {
            //회원 가입 로직 및 성공시 Main 이동
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}