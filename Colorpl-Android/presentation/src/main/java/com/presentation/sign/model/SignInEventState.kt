package com.presentation.sign.model

sealed interface SignInEventState {
    data object NoSign : SignInEventState
    data object SignInSuccess : SignInEventState
    data class Error(val message: String) : SignInEventState
    data object Loading : SignInEventState
}