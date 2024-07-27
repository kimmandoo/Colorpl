package com.presentation.ticket

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.navGraphViewModels
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketFinishBinding
import com.presentation.base.BaseFragment
import com.presentation.viewmodel.TicketCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TicketFinishFragment :
    BaseFragment<FragmentTicketFinishBinding>(R.layout.fragment_ticket_finish) {

    private val viewModel: TicketCreateViewModel by hiltNavGraphViewModels(R.id.nav_ticket_graph)
    override fun initView() {
        Timber.d("${viewModel.description}")
    }
}