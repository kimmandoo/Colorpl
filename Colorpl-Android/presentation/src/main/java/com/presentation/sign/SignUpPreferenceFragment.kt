package com.presentation.sign

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentSignUpPreferenceBinding
import com.presentation.MainActivity
import com.presentation.base.BaseDialogFragment
import com.presentation.sign.model.SignUpEventState
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
        observeSignUp()
    }


    private fun initClickEvent() {
        binding.apply {
            val item = hashMapOf(
                includeMovie to Category.MOVIE,
                includeCircus to Category.CIRCUS_MAGIC,
                includeMusical to Category.MUSICAL,
                includeTheatre to Category.PLAY,
                includeExhibition to Category.EXHIBITION
            )

            //취향 클릭
            item.keys.forEach { view ->
                view.clPreference.setOnClickListener {
                    val selected = if (signUpViewModel.userPreference.getItemCount() < 2) {
                        item[view]?.let { item ->
                            signUpViewModel.userPreference.addOrRemove(
                                item.getTitle()
                            )
                        }
                        !it.isSelected
                    } else {
                        item[view]?.let { item ->
                            signUpViewModel.userPreference.remove(
                                item.getTitle()
                            )
                        }
                        false
                    }
                    listOf(it, view.ivIcon, view.tvType).forEach { child ->
                        child.isSelected = selected
                    }
                    view.isSelected = selected
                }
            }
            ivBack.setOnClickListener {
                signUpViewModel.userPreference.clear()
                navigatePopBackStack()
            }

            tvNext.setOnClickListener { //회원 가입 로직 및 성공시 Main 이동
                signUpViewModel.signUp()
            }
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

    private fun observeSignUp() {
        signUpViewModel.signUpEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is SignUpEventState.SignUpSuccess -> {
                        Timber.d("회원가입 성공")
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                    }

                    is SignUpEventState.Error -> {
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }


}