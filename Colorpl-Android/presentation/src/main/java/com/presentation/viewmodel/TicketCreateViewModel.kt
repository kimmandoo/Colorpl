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

    // view에서 viewmodel로 주는 방향은 안좋은 걸로 알고있는데, 일단 이렇게 해둠
    // domain 모듈에서 serializable 어노테이션 사용가능하면 safe args로 처리할 것임
    // string 5개를 넘기는 건 좀 아닌 것 같아서 일단 이렇게
    fun setTicketInfo(description: Description){
        _description.value = description
    }


}