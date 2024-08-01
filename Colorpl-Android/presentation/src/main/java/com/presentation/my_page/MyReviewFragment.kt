package com.presentation.my_page

import androidx.navigation.fragment.findNavController
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
                findNavController().navigate(R.id.action_fragment_my_review_to_fragment_review)
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
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date(),
                    space = "ignota",
                    seat = "commune"
                ),
                Ticket(
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date(),
                    space = "ignota",
                    seat = "commune"
                ),
                Ticket(
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date(),
                    space = "ignota",
                    seat = "commune"
                ),
                Ticket(
                    ticketId = 4706,
                    name = "Elijah Merritt",
                    date = Date(),
                    space = "ignota",
                    seat = "commune"
                )
            )
        )

    }

    private fun initClickEvent() {
        val unUse = binding.ivUnUseTicket
        val use = binding.ivUseTicket
        unUse.isSelected = true

        binding.ivUnUseTicket.setOnClickListener {
            it.isSelected = !it.isSelected
            use.isSelected = false
            binding.type = false
            binding.indicator.setTransactionX(0f)
        }

        binding.ivUseTicket.setOnClickListener {
            it.isSelected = !it.isSelected
            unUse.isSelected = false
            binding.type = true
            val distance = setDistanceX(unUse, use)
            binding.indicator.setTransactionX(distance)
        }

        binding.includeTop.ivBack.setOnClickListener {
            navigatePopBackStack()
        }
    }
}