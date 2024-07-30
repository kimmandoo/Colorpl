package com.presentation.sign

import android.content.Intent
import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpPreferenceBinding
import com.presentation.MainActivity
import com.presentation.base.BaseDialogFragment
import com.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpPreferenceFragment :
    BaseDialogFragment<FragmentSignUpPreferenceBinding>(R.layout.fragment_sign_up_preference) {

    private val signUpViewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.sign_nav_graph)

    override fun initView(savedInstanceState: Bundle?) {
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
            signUpViewModel.clearData()
            navigatePopBackStack()
        }

        

        binding.tvNext.setOnClickListener {
            //회원 가입 로직 및 성공시 Main 이동
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}