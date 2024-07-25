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
            listOf("가나다라마바사아자 차카타파하 가나다라 마바사아자차카타파하 가나 다라마바사 아자  차카타파 하 가나다라비가나다 라마바사아자차카 타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라비가나다라마바사아자차카 타파하가나다라마바 사 아자차카 타 파하가나다라마바 사 아자차카타파하가나다라비"
                , "가나다라마바 사아자 차카타파하가나 다라마바 사아자차 카  타파하가나 다라마바  사아 자 차카타파 하가나 라비가나다 마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라비가나다라마바사 차카타파하가나 다라마바사아 자차카타파하가나다라마 바사아자 차카타파 하가나다라비", "" +
                        "쩜", "개", "쩜")

        )
    }

    private fun initClickEvent(){
        binding.includeTop.ivBack.setOnClickListener {
            navigatePopBackStack()
        }
    }
}