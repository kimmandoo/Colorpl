package com.presentation.my_page

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentNoticeBinding
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.mypage.NoticeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeFragment : BaseFragment<FragmentNoticeBinding>(R.layout.fragment_notice) {

    val noticeAdapter: NoticeAdapter by lazy {
        NoticeAdapter()
    }

    override fun initView() {
        initAdapter()
        initClickEvent()
    }

    private fun initAdapter() {
        binding.rcNotice.apply {
            adapter = noticeAdapter
            itemAnimator = null
        }

        noticeAdapter.submitList(
            listOf("")
        )
    }

    private fun initClickEvent() {
        binding.includeTop.clBack.setOnClickListener {
            navigatePopBackStack()
        }
    }
}