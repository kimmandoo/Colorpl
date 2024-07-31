package com.presentation.sign

import android.content.Intent
import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpPreferenceBinding
import com.presentation.MainActivity
import com.presentation.base.BaseDialogFragment
import com.presentation.util.Category
import com.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class SignUpPreferenceFragment :
    BaseDialogFragment<FragmentSignUpPreferenceBinding>(R.layout.fragment_sign_up_preference) {

    private val signUpViewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.sign_nav_graph)

    override fun initView(savedInstanceState: Bundle?) {
        initClickEvent()
        observeCompleteButton()
    }


    private fun initClickEvent() {
        val item = hashMapOf(
            binding.includeMovie to Category.MOVIE,
            binding.includeCircus to Category.CIRCUS,
            binding.includeMusical to Category.MUSICAL,
            binding.includeTheatre to Category.THEATRE,
            binding.includeExhibition to Category.EXHIBITION
        )

        //취향 클릭
        item.keys.forEach { view ->
            view.clPreference.setOnClickListener {
                item[view]?.let { item -> signUpViewModel.userPreference.addOrRemove(item) }
                val selected = !it.isSelected
                it.isSelected = selected
                view.ivIcon.isSelected = selected
                view.tvType.isSelected = selected
                view.isSelected = selected
            }
        }


        binding.ivBack.setOnClickListener {
            signUpViewModel.userPreference.clear()
            navigatePopBackStack()
        }



        binding.tvNext.setOnClickListener { //회원 가입 로직 및 성공시 Main 이동
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun observeCompleteButton() { //완료 버튼 활성화
        signUpViewModel.completeButton.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                Timber.d("완료 아이템 활성화 $it")
                binding.tvNext.apply {
                    isSelected = it
                    isEnabled = it
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


}