package com.presentation.sign.model

sealed interface SignInEventState {

    data object SignInSuccess : SignInEventState
    data class Error(val message: String) : SignInEventState

}