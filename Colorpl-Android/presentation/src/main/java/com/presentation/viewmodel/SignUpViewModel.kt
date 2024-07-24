package com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.presentation.util.Sign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(

) : ViewModel() {


    private val _typeEvent = MutableSharedFlow<Sign>()
    val typeEvent : SharedFlow<Sign>
        get() = _typeEvent

    fun setTypeEvent(type : Sign){
        viewModelScope.launch {
            _typeEvent.emit(type)
        }
    }
}