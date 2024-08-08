package com.presentation.my_page

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.FragmentUserSearchBinding
import com.presentation.base.BaseFragment
import com.presentation.component.adapter.mypage.UserSearchAdapter
import com.presentation.util.imeOptionsActionCheck
import com.presentation.viewmodel.MemberSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MemberSearchFragment :
    BaseFragment<FragmentUserSearchBinding>(R.layout.fragment_user_search) {
    private val memberSearchViewModel: MemberSearchViewModel by viewModels()
    private lateinit var userSearchAdapter: UserSearchAdapter

    override fun initView() {
        initAdapter()
        initClickEvent()
        observeSearchText()
        observeMemberSearch()
    }

    private fun observeSearchText() {
        binding.etSearch.imeOptionsActionCheck {
            memberSearchViewModel.getMemberSearchInfo(binding.etSearch.text.toString())
            binding.etSearch.clearFocus()
        }
    }

    private fun observeMemberSearch() {
        memberSearchViewModel.memberSearchInfo.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                userSearchAdapter.submitList(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initAdapter() {
        userSearchAdapter = UserSearchAdapter()
        binding.rcSearch.apply {
            adapter = userSearchAdapter
            itemAnimator = null
        }


    }

    private fun initClickEvent() {
        binding.includeTop.ivBack.setOnClickListener {
            navigatePopBackStack()
        }
    }
}