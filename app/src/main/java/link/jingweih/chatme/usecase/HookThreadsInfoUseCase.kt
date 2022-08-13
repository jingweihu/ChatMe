package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import link.jingweih.chatme.repository.ThreadRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseFlowUseCase
import javax.inject.Inject

class HookThreadsInfoUseCase @Inject constructor(
    private val threadRepository: ThreadRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseFlowUseCase<Unit, Unit>(ioDispatcher) {
    override fun execute(input: Unit): Flow<Unit> {
        return threadRepository.getThreads().map { threadList ->
            threadList.map { thread ->
                if (!threadRepository.isThreadReady(thread.threadId)) {
                    threadRepository.saveThreadInfo(
                        thread,
                        threadRepository.getMemberInfo(thread.threadId)
                    )
                } else {
                    threadRepository.saveThread(thread)
                }
            }
        }
    }
}