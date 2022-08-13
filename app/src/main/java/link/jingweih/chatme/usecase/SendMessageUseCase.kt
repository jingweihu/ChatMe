package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.repository.ChatRepository
import link.jingweih.chatme.responses.MessageType
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseUseCase
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val chatRepository: ChatRepository
) :
    BaseUseCase<SendMessageInput, Boolean>(ioDispatcher) {
    override suspend fun execute(input: SendMessageInput): Boolean {
        return chatRepository.sendMessage(
            threadInfo = input.chatThreadWithMembers,
            message = input.message,
            type = input.type
        )
    }
}

data class SendMessageInput(
    val message: String,
    val type: MessageType,
    val chatThreadWithMembers: ChatThreadWithMembers
)