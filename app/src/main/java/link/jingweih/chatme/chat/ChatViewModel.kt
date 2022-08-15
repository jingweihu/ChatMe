package link.jingweih.chatme.chat

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import link.jingweih.chatme.domain.ChatMessage
import link.jingweih.chatme.domain.ChatThread
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.responses.MessageType
import link.jingweih.chatme.usecase.GetThreadChatsUseCase
import link.jingweih.chatme.usecase.SendMessageInput
import link.jingweih.chatme.usecase.SendMessageUseCase
import link.jingweih.jingwei.core.framework.domain.Result

class ChatViewModel @AssistedInject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    getThreadChatsUseCase: GetThreadChatsUseCase,
    @Assisted
    private val navArgs: ChatFragmentArgs
) : ViewModel() {

    private val _chatUiState = MutableLiveData<ChatUiState>()
    val chatUiState: LiveData<ChatUiState> = _chatUiState

    @AssistedFactory
    interface ChatViewModelFactory {
        fun create(navArgs: ChatFragmentArgs): ChatViewModel
    }

    companion object {
        fun providesFactory(
            assistedFactory: ChatViewModelFactory,
            navArgs: ChatFragmentArgs
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(navArgs) as T
            }
        }
    }

    init {
        getThreadChatsUseCase(navArgs)
            .onEach {
                _chatUiState.value = ChatUiState.Success(it)
            }.catch { e ->
                ChatUiState.Failure(e.message)
            }.launchIn(viewModelScope)
    }

    fun sendMessage(
        message: String,
        type: MessageType = MessageType.MESSAGE,
    ) {
        viewModelScope.launch {
            when (val result = sendMessageUseCase(
                SendMessageInput(
                    message = message,
                    type = type,
                    chatThread = navArgs.chatThreadWithMembers.chatThread
                )
            )) {
                is Result.Error -> _chatUiState.value =
                    ChatUiState.Failure(result.exception.message)
                is Result.Success -> _chatUiState.value =
                    ChatUiState.Send
            }
        }
    }
}


sealed class ChatUiState {
    data class Success(val messages: List<ChatMessage>) : ChatUiState()
    data class Failure(val error: String?) : ChatUiState()
    object Send : ChatUiState()
}