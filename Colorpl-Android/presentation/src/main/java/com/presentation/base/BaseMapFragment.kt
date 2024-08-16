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
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.presentation.component.dialog.LoadingDialog

abstract class BaseMapFragment<T : ViewDataBinding>(private val layoutResId: Int) :
    Fragment(),
    OnMapReadyCallback {

    private var _binding: T? = null
    val binding get() = _binding!!
    val loading by lazy {
        LoadingDialog(requireActivity())
    }
    abstract var mapView: MapView?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initOnCreateView()
        return binding.root
    }

    abstract fun initOnCreateView()

    override fun onMapReady(naverMap: NaverMap) = initOnMapReady(naverMap)

    abstract fun initOnMapReady(naverMap: NaverMap)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViewCreated()
    }

    abstract fun iniViewCreated()

    fun navigateDestination(@IdRes action: Int) { //Navigation 이동
        findNavController().navigate(action)
    }

    fun navigateDestination(action: NavDirections) { //Navigation 이동
        findNavController().navigate(action)
    }

    fun dismissLoading() {
        loading.dismiss()
    }

    fun showLoading() {
        loading.show()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }


    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}
