package com.presentation.sign.model

sealed class SignInEventState {

    data object SignInSuccess : SignInEventState()
    data class Error(val message: String) : SignInEventState()

}