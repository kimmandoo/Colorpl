package com.presentation.my_page

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMyPageBinding
import com.domain.model.Ticket
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.schedule.TicketAdapter
import com.presentation.util.setDistanceX
import com.presentation.util.setTransactionX
import com.presentation.viewmodel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.Date

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel: MyPageViewModel by viewModels()

    private val ticketAdapter: TicketAdapter by lazy {
        TicketAdapter(
            onTicketClickListener = {

            }
        )
    }


    override fun initView() {
        initTicket()
        initClickEvent()
        observeUiState()
    }

    private fun initTicket() {
        binding.rcTicket.apply {
            adapter = ticketAdapter
            itemAnimator = null
        }


        ticketAdapter.submitList(
            listOf(
                // testcode
                Ticket(
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date().toString(),
                    theater = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
                Ticket(
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date().toString(),
                    theater = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
                Ticket(
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date().toString(),
                    theater = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
                Ticket(
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date().toString(),
                    theater = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
            )
        )
    }

    private fun observeUiState() {
        myPageViewModel.memberUiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.member = it.memberInfo
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun initClickEvent() {
        val star = binding.ivTicketStar
        val expire = binding.ivTicketExpire
        star.isSelected = true

        binding.apply {
            ivTicketStar.setOnClickListener {
                it.isSelected = !it.isSelected
                expire.isSelected = false
                indicator.setTransactionX(0f)
            }
            ivTicketExpire.setOnClickListener {
                it.isSelected = !it.isSelected
                star.isSelected = false
                val distance = setDistanceX(binding.ivTicketStar, binding.ivTicketExpire)
                indicator.setTransactionX(distance)
            }
            tvProfileImg.setOnClickListener { //프로필 수정 이동
                navigateDestination(
                    R.id.action_fragment_my_page_to_fragment_profile_update
                )
            }
            includeSearchUser.clMenu.setOnClickListener { // 유저 찾기 이동
                navigateDestination(R.id.action_fragment_my_page_to_fragment_user_search)
            }
            includeMyReview.clMenu.setOnClickListener { // 나의 리뷰 이동
                navigateDestination(R.id.action_fragment_my_page_to_fragment_my_review)
            }
            includeNotice.clMenu.setOnClickListener { //공지사항 이동
                navigateDestination(R.id.action_fragment_my_page_to_fragment_notice)
            }
            includePayment.clMenu.setOnClickListener { //결제 내역 이동
                navigateDestination(R.id.action_fragment_my_page_to_fragment_payment_history)
            }
            ivSetting.setOnClickListener { //설정 이동
                navigateDestination(R.id.action_fragment_my_page_to_fragment_setting)
            }
            imgFeedRegister.setOnClickListener {
                navigateDestination(R.id.action_fragment_my_page_to_fragment_feed_ticket_select)
            }
        }
    }

}