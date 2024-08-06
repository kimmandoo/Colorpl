package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Review
import com.domain.usecase.ReviewCreateUseCase
import com.domain.util.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewCreateUseCase: ReviewCreateUseCase
) : ViewModel() {
    private val _selectedEmotion = MutableStateFlow<Int>(-1)
    val selectedEmotion: StateFlow<Int> = _selectedEmotion
    private val _editTextCheck = MutableStateFlow(false)
    private val _confirmCheck = MutableStateFlow(false)
    val confirmCheck: StateFlow<Boolean> = _confirmCheck
    private val _reviewResponse = MutableSharedFlow<Int>()
    val reviewResponse: SharedFlow<Int> = _reviewResponse.asSharedFlow()

    fun setEmotion(index: Int) {
        _selectedEmotion.value = index
        checkConfirm()
    }

    fun checkEditText(length: Int?) {
        _editTextCheck.value = (length != null) && (length > 50)
        checkConfirm()
    }

    private fun checkConfirm() {
        _confirmCheck.value = _editTextCheck.value == true && _selectedEmotion.value != -1
    }

    fun createReview(review: Review, image: File? = null) {
        viewModelScope.launch {
            reviewCreateUseCase(image, review)
                .collect { response ->
                    when (response) {
                        is DomainResult.Success -> {
                            _reviewResponse.emit(response.data)
                        }

                        is DomainResult.Error -> {
                            _reviewResponse.emit(-1)
                        }
                    }
                }
        }
    }
}