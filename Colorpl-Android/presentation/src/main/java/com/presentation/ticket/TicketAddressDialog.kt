package com.presentation.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colorpl.presentation.R
import com.colorpl.presentation.databinding.DialogAddressBinding
import com.presentation.base.BaseDialogFragment
import com.presentation.viewmodel.TicketCreateViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class TicketAddressDialog :
    BaseDialogFragment<DialogAddressBinding>(R.layout.dialog_address) {
    private lateinit var webView: WebView

    private val viewModel: TicketCreateViewModel by hiltNavGraphViewModels(R.id.nav_ticket_graph)

    override fun initView(savedInstanceState: Bundle?) {
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.locationIvBack.setOnClickListener { navigatePopBackStack() }
        webView = binding.locationSearchWebView
        webView.clearCache(true)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(Bridge(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }
        webView.loadUrl("https://colorpl.web.app")
    }

    inner class Bridge() {
        @JavascriptInterface
        fun processDATA(fullRoadAddr: String) {
            lifecycleScope.launch {
                Timber.tag("address").d("Full Road Address: $fullRoadAddr")
                // 여기에서 주소 데이터를 처리하는 로직을 추가합니다.
                viewModel.getAddress(fullRoadAddr)
                findNavController().popBackStack()
            }
        }
    }
}