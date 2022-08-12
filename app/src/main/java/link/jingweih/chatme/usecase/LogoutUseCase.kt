package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import link.jingweih.chatme.repository.AccountRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseUseCase
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
): BaseUseCase<Unit, Boolean>(ioDispatcher) {
    override suspend fun execute(input: Unit): Boolean {
        accountRepository.logout()
        return true
    }
}