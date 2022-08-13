package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.repository.ThreadRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseFlowUseCase
import javax.inject.Inject

class GetThreadsUseCase @Inject constructor(
    private val threadRepository: ThreadRepository,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher
) : BaseFlowUseCase<Unit, List<ChatThreadWithMembers>>(ioDispatcher) {
    override fun execute(input: Unit): Flow<List<ChatThreadWithMembers>> {
        return threadRepository.getChatThreadWithMembers()
    }
}