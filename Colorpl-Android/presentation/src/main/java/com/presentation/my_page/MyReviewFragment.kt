package com.presentation.my_page

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMyReviewBinding
import com.domain.model.Ticket
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.schedule.TicketAdapter
import com.presentation.util.setDistanceX
import com.presentation.util.setTransactionX
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class MyReviewFragment : BaseFragment<FragmentMyReviewBinding>(R.layout.fragment_my_review) {

    private val ticketAdapter: TicketAdapter by lazy {
        TicketAdapter(
            onTicketClickListener = {
            }
        )
    }

    override fun initView() {
        initTicket()
        initClickEvent()
    }

    private fun initTicket() {
        binding.rcReview.apply {
            adapter = ticketAdapter
            itemAnimator = null
        }


        ticketAdapter.submitList(
            listOf( // testcode
                Ticket(
                    name = "Elijah Merritt",
                    dateTime = Date().toString(),
                    location = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
                Ticket(
                    name = "Elijah Merritt",
                    dateTime = Date().toString(),
                    location = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
                Ticket(
                    name = "Elijah Merritt",
                    dateTime = Date().toString(),
                    location = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
                Ticket(
                    name = "Elijah Merritt",
                    dateTime = Date().toString(),
                    location = "ignota",
                    seat = "commune",
                    category = "뮤지컬"
                ),
            )
        )

    }

    private fun initClickEvent() {
        val unUse = binding.ivUnUseTicket
        val use = binding.ivUseTicket
        unUse.isSelected = true

        binding.apply {
            ivUnUseTicket.setOnClickListener {
                it.isSelected = !it.isSelected
                use.isSelected = false
                type = false
                indicator.setTransactionX(0f)
            }

            ivUseTicket.setOnClickListener {
                it.isSelected = !it.isSelected
                unUse.isSelected = false
                type = true
                val distance = setDistanceX(unUse, use)
                indicator.setTransactionX(distance)
            }

            includeTop.ivBack.setOnClickListener {
                navigatePopBackStack()
            }

            imgFeedRegister.setOnClickListener {
                navigateDestination(R.id.action_fragment_my_review_to_fragment_feed_ticket_select)
            }
        }
    }
}