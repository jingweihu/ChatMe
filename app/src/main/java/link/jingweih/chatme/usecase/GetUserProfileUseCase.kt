package link.jingweih.chatme.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import link.jingweih.chatme.domain.Profile
import link.jingweih.chatme.repository.AccountRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseFlowUseCase
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : BaseFlowUseCase<Unit, Profile>(ioDispatcher) {

    override fun execute(input: Unit): Flow<Profile> {
        return accountRepository.getProfile()
    }
}