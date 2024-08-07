package com.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class BaseFragment<T : ViewDataBinding>(private val layoutResId: Int) : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun initView()

    //Navigation 이동
    fun navigateDestination(@IdRes action: Int) {
        findNavController().navigate(action)
    }

    fun navigateDestinationBundle(@IdRes action: Int, bundle: Bundle) {
        findNavController().navigate(action, bundle)
    }

    //Navigation safe args 이동
    fun navigateDestination(action: NavDirections) {
        findNavController().navigate(action)
    }

    //popBackstack
    fun navigatePopBackStack() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
