package com.presentation.ticket

import android.os.Bundle
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentTicketFinishBinding
import com.presentation.base.BaseFragment
import com.presentation.notification.FcmWorker
import com.presentation.util.hourToMills
import com.presentation.util.onBackButtonPressed
import com.presentation.util.stringToCalendar
import com.presentation.viewmodel.TicketCreateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TicketFinishFragment :
    BaseFragment<FragmentTicketFinishBinding>(R.layout.fragment_ticket_finish) {

    private val viewModel: TicketCreateViewModel by hiltNavGraphViewModels(R.id.nav_ticket_graph)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackButtonPressed(this) {
        }
    }

    override fun initView() {
        observeViewModel()
        initUi()
    }

    private fun observeViewModel() {
        viewModel.createResponse.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { ticketId ->
                when {
                    ticketId >= 0 -> {
                        initWorkerManager()
                        loading.dismiss()
                        binding.tvConfirm.isEnabled = true
                    }

                    ticketId < 0 -> {
                        dismissLoading()
                        Toast.makeText(requireContext(), "티켓 생성에 실패했습니다", Toast.LENGTH_SHORT).show()
                        binding.tvConfirm.isEnabled = true
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initWorkerManager() {
        val description = viewModel.description.value
        val ticketDate = stringToCalendar(description?.schedule ?: "")?.timeInMillis ?: 0
        val currentTime = System.currentTimeMillis()
        val delay = ticketDate - hourToMills(4) - currentTime
        val latLng = viewModel.geocodingLatLng.value
        val data = Data.Builder()
            .putString("latLng", "${latLng.latitude},${latLng.longitude}")
            .build()
        Timber.d("지도 데이터 확인 ${viewModel.geocodingLatLng.value}")
        val fcmWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<FcmWorker>()
            .setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        val workManager = WorkManager.getInstance(requireActivity())
        workManager.enqueue(fcmWorkRequest)
    }

    private fun initUi() {
        val description = viewModel.description.value
        binding.apply {
            tvTitle.text = description?.title
            tvDetail.text = description?.detail
            tvSchedule.text = description?.schedule
            tvSeat.text = description?.seat
            tvConfirm.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {

                    tvConfirm.isEnabled = false
                    showLoading()
                }
            }
        }
    }
}