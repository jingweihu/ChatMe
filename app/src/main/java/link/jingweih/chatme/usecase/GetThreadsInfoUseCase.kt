package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import link.jingweih.chatme.domain.ChatThreadWithMembers
import link.jingweih.chatme.repository.ThreadRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseFlowUseCase
import javax.inject.Inject

class GetThreadsInfoUseCase @Inject constructor(
    private val threadRepository: ThreadRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): BaseFlowUseCase<Unit, List<ChatThreadWithMembers>>(ioDispatcher) {
    override fun execute(input: Unit): Flow<List<ChatThreadWithMembers>> {
        return threadRepository.getThreads().map { threadList ->
             val result = threadList.map { thread ->
                 threadRepository.saveThread(thread)
                 if (!threadRepository.isThreadReady(thread.threadId)) {
                     val thread = threadRepository.getThreadInfo(thread)
                     threadRepository.saveThreadInfo(thread)
                 }
             }
            threadRepository.getChatThreadWithMembers()
        }
    }
}