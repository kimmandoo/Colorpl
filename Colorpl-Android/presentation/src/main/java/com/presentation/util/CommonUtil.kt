package com.presentation.util

fun Boolean.doOnTrue(func: () -> Unit): Boolean {
    if (this) {
        func()
    }
    return this
}

fun Boolean.doOnFalse(func: () -> Unit): Boolean {
    if (this.not()) {
        func()
    }
    return this
}