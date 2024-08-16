package com.presentation.my_page.model

import com.domain.model.Member

data class MemberUiState(
    val memberInfo : Member? = Member() ,
    val check : Boolean ?= false
)
