package com.presentation.sign.model

interface SignUpEventState {

    data object SignUpSuccess : SignUpEventState

    data class Error(val message: String) : SignUpEventState
}