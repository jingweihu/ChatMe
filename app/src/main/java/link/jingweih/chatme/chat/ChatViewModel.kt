package link.jingweih.chatme.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.responses.MessageType
import link.jingweih.chatme.usecase.SendMessageInput
import link.jingweih.chatme.usecase.SendMessageUseCase
import link.jingweih.jingwei.core.framework.domain.Result
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _chatUiState = MutableLiveData<ChatUiState>()
    val chatUiState: LiveData<ChatUiState> = _chatUiState

    fun sendMessage(
        message: String,
        threadInfo: ChatThreadWithMembers,
        type: MessageType = MessageType.MESSAGE,
    ) {
        viewModelScope.launch {
            when (val result = sendMessageUseCase(
                SendMessageInput(
                    message = message,
                    type = type,
                    chatThreadWithMembers = threadInfo
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
    data class Failure(val error: String?) : ChatUiState()
    object Send: ChatUiState()
}