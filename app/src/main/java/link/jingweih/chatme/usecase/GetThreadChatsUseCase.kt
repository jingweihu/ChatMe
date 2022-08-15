package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import link.jingweih.chatme.chat.ChatFragmentArgs
import link.jingweih.chatme.domain.ChatMessage
import link.jingweih.chatme.repository.ChatRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseFlowUseCase
import javax.inject.Inject

class GetThreadChatsUseCase @Inject constructor(
    @IODispatcher ioDispatcher: CoroutineDispatcher,
    private val chatRepository: ChatRepository
) : BaseFlowUseCase<ChatFragmentArgs, List<ChatMessage>>(ioDispatcher) {
    override fun execute(input: ChatFragmentArgs): Flow<List<ChatMessage>> {
        return chatRepository.getMessages(chatFragmentArgs = input)
    }
}