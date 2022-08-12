package link.jingweih.chatme.thread.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import link.jingweih.chatme.usecase.CreateThreadUseCase
import link.jingweih.jingwei.core.framework.domain.Result
import javax.inject.Inject

@HiltViewModel
class CreateThreadViewModel @Inject constructor(
    private val createThreadUseCase: CreateThreadUseCase): ViewModel() {

    private val _createThreadUiState = MutableLiveData<CreateThreadUiState>()
    val createThreadUiState: LiveData<CreateThreadUiState> = _createThreadUiState

    fun createThread(friendEmail: String) {
        viewModelScope.launch {
            when(val result = createThreadUseCase(friendEmail)) {
                is Result.Success -> _createThreadUiState.value = CreateThreadUiState.Success
                is Result.Error -> _createThreadUiState.value =
                    CreateThreadUiState.Failure(result.exception.message)
            }
        }
    }
}

sealed class CreateThreadUiState {
    object Success : CreateThreadUiState()
    data class Failure(val error: String?) : CreateThreadUiState()
}