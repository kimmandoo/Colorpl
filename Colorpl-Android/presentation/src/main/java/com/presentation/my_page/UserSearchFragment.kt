package com.presentation.my_page

import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentUserSearchBinding
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.mypage.UserSearchAdapter
import com.presentation.util.imeOptionsActionCheck
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSearchFragment : BaseFragment<FragmentUserSearchBinding>(R.layout.fragment_user_search) {

    private lateinit var userSearchAdapter: UserSearchAdapter

    override fun initView() {
        initAdapter()
        initClickEvent()
        observeSearchText()
    }

    private fun observeSearchText() {
        binding.etSearch.imeOptionsActionCheck {
            binding.etSearch.clearFocus()
        }
    }

    private fun initAdapter() {
        userSearchAdapter = UserSearchAdapter()
        binding.rcSearch.apply {
            adapter = userSearchAdapter
            itemAnimator = null
        }

        userSearchAdapter.submitList(
            listOf("", "", "", "", "", "")
        )
    }

    private fun initClickEvent() {
        binding.includeTop.ivBack.setOnClickListener {
            navigatePopBackStack()
        }
    }
}