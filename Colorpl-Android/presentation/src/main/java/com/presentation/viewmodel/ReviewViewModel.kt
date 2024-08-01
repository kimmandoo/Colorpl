package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(

) : ViewModel() {
    private val _selectedEmotion = MutableStateFlow<Int>(-1)
    val selectedEmotion: StateFlow<Int> = _selectedEmotion
    private val _editTextCheck = MutableStateFlow(false)
    private val _confirmCheck = MutableStateFlow(false)
    val confirmCheck: StateFlow<Boolean> = _confirmCheck


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


}