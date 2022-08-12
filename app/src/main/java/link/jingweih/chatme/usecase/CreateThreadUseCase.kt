package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import link.jingweih.chatme.repository.ThreadRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseUseCase
import javax.inject.Inject

class CreateThreadUseCase @Inject constructor(
    private val threadRepository: ThreadRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
): BaseUseCase<String, Boolean>(ioDispatcher) {
    override suspend fun execute(friendEmail: String): Boolean {
        return threadRepository.createThread(friendEmail)
    }
}