package com.presentation.my_page

import android.animation.ObjectAnimator
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentMyPageBinding
import com.domain.model.Ticket
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.schedule.TicketAdapter
import com.presentation.util.dpToPxF
import com.presentation.util.setDistanceX
import com.presentation.util.setTransactionX
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val ticketAdapter : TicketAdapter by lazy {
        TicketAdapter(
            onTicketClickListener = {

            }
        )
    }


    override fun initView() {
        initTicket()
        initClickEvent()
    }

    private fun initTicket(){
        binding.rcTicket.adapter = ticketAdapter

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
        val star = binding.ivTicketStar
        val expire = binding.ivTicketExpire
        star.isSelected = true

        binding.ivTicketStar.setOnClickListener {
            it.isSelected = !it.isSelected
            expire.isSelected = false
            binding.indicator.setTransactionX(0f)
        }

        binding.ivTicketExpire.setOnClickListener {
            it.isSelected = !it.isSelected
            star.isSelected = false
            val distance = setDistanceX(binding.ivTicketStar, binding.ivTicketExpire)
            binding.indicator.setTransactionX(distance)
        }
    }

}