package com.presentation.feed

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentFeedTicketSelectBinding
import com.presentation.base.BaseDialogFragment
import com.presentation.component.adapter.feed.FeedTicketSelectAdapter
import com.presentation.util.addCustomItemDecoration
import com.presentation.util.toLocalDate
import com.presentation.viewmodel.TicketSelectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate


@AndroidEntryPoint
class FeedTicketSelectFragment :
    BaseDialogFragment<FragmentFeedTicketSelectBinding>(R.layout.fragment_feed_ticket_select) {

    private val viewModel: TicketSelectViewModel by viewModels()
    private val feedTicketSelectAdapter by lazy {
        FeedTicketSelectAdapter { }
    }

    override fun initView(savedInstanceState: Bundle?) {
        observeViewModel()
        initAdapter()
        initClickEvent()
    }

    private fun observeViewModel() {
        showLoading()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.tickets.collectLatest { unreviewedList ->
                    feedTicketSelectAdapter.submitList(unreviewedList.filter { it.dateTime.toLocalDate() < LocalDate.now() })
                    if (feedTicketSelectAdapter.currentList.isEmpty()) {
                        binding.tvTitle.text = "리뷰를 남길 티켓이 없습니다"
                        binding.tvSelect.text = "뒤로 돌아가기"
                        binding.tvSelect.setOnClickListener {
                            navigatePopBackStack()
                        }
                    } else {
                        binding.tvSelect.setOnClickListener {
                            val action =
                                FeedTicketSelectFragmentDirections.actionFragmentFeedTicketSelectToFragmentReview(
                                    unreviewedList[binding.vpFeedTicketSelect.currentItem].id
                                )
                            navigateDestination(action)
                        }
                    }
                    dismissLoading()
                }
            }
        }
    }

    private fun initAdapter() {
        binding.vpFeedTicketSelect.apply {
            adapter = feedTicketSelectAdapter
            offscreenPageLimit = 1
            this.addCustomItemDecoration()
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                }
            })
        }

    }

    private fun initClickEvent() {
        binding.apply {
            ivBack.setOnClickListener {
                navigatePopBackStack()
            }
        }
    }
}