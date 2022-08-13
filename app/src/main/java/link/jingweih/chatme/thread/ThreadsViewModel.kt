package link.jingweih.chatme.thread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.usecase.GetThreadsUseCase
import link.jingweih.chatme.usecase.HookThreadsInfoUseCase
import javax.inject.Inject

@HiltViewModel
class ThreadsViewModel @Inject constructor(
    hookThreadsInfoUseCase: HookThreadsInfoUseCase,
    getThreadsUseCase: GetThreadsUseCase,
) : ViewModel() {

    private val _threadsUiState = MutableLiveData<ThreadsUiState>()
    val threadsUiState: LiveData<ThreadsUiState> = _threadsUiState


    init {
        hookThreadsInfoUseCase(Unit)
            .launchIn(viewModelScope)

        getThreadsUseCase(Unit)
            .onEach {
                _threadsUiState.value = ThreadsUiState.Success(it)
            }
            .catch { e ->
                _threadsUiState.value = ThreadsUiState.Failure(e.message)
            }
            .launchIn(viewModelScope)
    }
}

sealed class ThreadsUiState {
    data class Success(val threads: List<ChatThreadWithMembers>) : ThreadsUiState()
    data class Failure(val error: String?) : ThreadsUiState()
}


