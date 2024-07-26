package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Description
import com.domain.usecase.OpenAiUseCase
import com.domain.util.RepoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketCreateViewModel @Inject constructor(
    private val openAiUseCase: OpenAiUseCase
): ViewModel() {
    private val _description = MutableStateFlow<Description?>(null)
    val description: StateFlow<Description?> = _description

    fun getDescription(base64String: String){
        viewModelScope.launch {
            openAiUseCase(base64String).collectLatest {
                when (it) {
                    is RepoResult.Success -> {
                        _description.value = it.data
                    }

                    is RepoResult.Error -> {

                    }
                }
            }
        }
    }


}