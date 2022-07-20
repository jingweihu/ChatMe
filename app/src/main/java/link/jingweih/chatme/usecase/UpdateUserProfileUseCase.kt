package link.jingweih.chatme.usecase

import android.net.Uri
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.CoroutineDispatcher
import link.jingweih.chatme.profile.Profile
import link.jingweih.chatme.repository.AccountRepository
import link.jingweih.jingwei.core.framework.concurrent.IODispatcher
import link.jingweih.jingwei.core.framework.domain.BaseUseCase
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : BaseUseCase<UseProfile, Profile>(ioDispatcher) {

    override suspend fun execute(input: UseProfile): Profile {
        val request = userProfileChangeRequest {
            displayName = input.displayName
            photoUri = Uri.parse(input.photoUri)
        }
        return Profile.fromFirebaseUser(accountRepository.updateProfile(request))
    }
}

data class UseProfile(
    val displayName: String,
    val photoUri: String,
)