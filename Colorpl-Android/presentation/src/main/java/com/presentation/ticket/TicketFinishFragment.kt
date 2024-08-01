package com.presentation.ticket

import android.os.Bundle
import android.util.Log
import androidx.core.net.toFile
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketFinishBinding
import com.domain.model.TicketCreate
import com.domain.model.TicketTest
import com.presentation.base.BaseFragment
import com.presentation.util.ImageProcessingUtil
import com.presentation.util.onBackButtonPressed
import com.presentation.viewmodel.TicketCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TicketFinishFragment :
    BaseFragment<FragmentTicketFinishBinding>(R.layout.fragment_ticket_finish) {

    private val viewModel: TicketCreateViewModel by hiltNavGraphViewModels(R.id.nav_ticket_graph)
    private val args: TicketFinishFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackButtonPressed(this){
            findNavController().navigate(R.id.action_fragment_ticket_finish_to_fragment_schedule)
        }
    }

    override fun initView() {
        initUi()
    }

    private fun initUi() {
        Glide.with(binding.root.context).load(args.imageUrl).centerCrop().into(binding.ivPoster)
        val description = viewModel.description.value
        binding.apply {
            tvTitle.text = description?.title
            tvDetail.text = description?.detail
            tvSchedule.text = description?.schedule
            tvSeat.text = description?.seat
            tvConfirm.setOnClickListener {
                // 서버에 등록된 티켓 보내고 navigate 돌리기
                Timber.d("${ImageProcessingUtil(binding.root.context).uriToFile(args.imageUrl!!)!!}  size: ${ImageProcessingUtil(binding.root.context).uriToFile(args.imageUrl!!)!!.length()}")
                viewModel.createTicket(ImageProcessingUtil(binding.root.context).uriToFile(args.imageUrl!!)!!)
                // 보내는 동안 저장중 띄우기
//                findNavController().navigate(R.id.action_fragment_ticket_finish_to_fragment_schedule)
            }
        }
    }
}