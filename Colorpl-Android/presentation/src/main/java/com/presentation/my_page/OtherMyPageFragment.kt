package com.presentation.my_page

import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentOtherMyPageBinding
import com.presentation.base.BaseFragment
import com.presentation.my_page.model.MyPageEventState
import com.presentation.my_page.model.OtherMyPageEventState
import com.presentation.util.setDistanceX
import com.presentation.util.setImageCircleCrop
import com.presentation.util.setTransactionX
import com.presentation.viewmodel.MyPageViewModel
import com.presentation.viewmodel.OtherMyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class OtherMyPageFragment :
    BaseFragment<FragmentOtherMyPageBinding>(R.layout.fragment_other_my_page) {
    private val myPageViewModel: MyPageViewModel by viewModels()
    private val otherMyPageViewModel: OtherMyPageViewModel by viewModels()

    override fun initView() {
        initData()
        initClickEvent()
        observeUiState()
        observeEventState()
    }


    private fun initData() {
        val safeArgs: OtherMyPageFragmentArgs by navArgs()
        safeArgs.memberInfo.apply {
            Timber.d("데이ㅓㅌ 확인 $this")
            binding.isFollow = isFollowing
            binding.tvFollowRequest.isSelected = isFollowing
            otherMyPageViewModel.setFollowerId(memberId)
            myPageViewModel.getMemberInfo(true, memberId)
        }

    }


    private fun observeUiState() {
        myPageViewModel.memberUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.member = it.memberInfo
                binding.ivProfileImg.setImageCircleCrop(it.memberInfo?.profileImage, true)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEventState() {
        otherMyPageViewModel.otherMyPageEventState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                myPageViewModel.getMemberInfo(true, otherMyPageViewModel.followerId.value)
                when (it) {
                    is OtherMyPageEventState.Follow -> {
                        binding.isFollow = true
                        binding.tvFollowRequest.isSelected = true
                    }

                    is OtherMyPageEventState.UnFollow -> {
                        binding.isFollow = false
                        binding.tvFollowRequest.isSelected = false
                    }

                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        myPageViewModel.myPageEventState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is MyPageEventState.UseTicket -> {
                        binding.apply {
                            ticketTitle = true
                            ticketCount = it.data.size.toString()
                            ivTicketExpire.isSelected = true
                            ivTicketStar.isSelected = false
                            val distance =
                                setDistanceX(binding.ivTicketStar, binding.ivTicketExpire)
                            indicator.setTransactionX(distance)
                        }

                    }

                    is MyPageEventState.UnUseTicket -> {
                        binding.apply {
                            ticketTitle = false
                            ticketCount = it.data.size.toString()
                            ivTicketStar.isSelected = true
                            ivTicketExpire.isSelected = false
                            indicator.setTransactionX(0f)
                        }
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initClickEvent() {
        binding.apply {
            clBack.setOnClickListener {
                navigatePopBackStack()
            }
            ivTicketStar.postDelayed(300L) {
                binding.ivTicketStar.performClick()
            }
            ivTicketStar.setOnClickListener {
                myPageViewModel.ticketEvent(UN_USE)

            }
            ivTicketExpire.setOnClickListener {
                myPageViewModel.ticketEvent(USE)
            }
            tvFollowRequest.setOnClickListener {
                if (!binding.tvFollowRequest.isSelected) {
                    otherMyPageViewModel.follow()
                } else {
                    otherMyPageViewModel.unFollow()
                }
            }
        }


    }

    companion object {
        const val USE = true
        const val UN_USE = false
    }
}
