package link.jingweih.chatme.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import link.jingweih.chatme.profile.Profile
import link.jingweih.chatme.usecase.UpdateUserProfileUseCase
import link.jingweih.chatme.usecase.UseProfile
import link.jingweih.jingwei.core.framework.domain.Result
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _accountUiState = MutableLiveData<AccountUiState>()
    val accountUiState: LiveData<AccountUiState> = _accountUiState

    init {
        auth.currentUser?.let {
            _accountUiState.value = AccountUiState.Success(
                profile = Profile.fromFirebaseUser(it)
            )
        }

    }

    fun saveProfile(displayName: String, photoUri: String) {
        viewModelScope.launch {
            when (val result = updateUserProfileUseCase(
                UseProfile(displayName = displayName, photoUri = photoUri)
            )) {
                is Result.Success -> _accountUiState.value = AccountUiState.Success(result.data)
                is Result.Error -> _accountUiState.value =
                    AccountUiState.Failure(result.exception.message)
            }
        }

    }

    fun logout() {
        auth.signOut()
    }

}

sealed class AccountUiState {
    data class Success(val profile: Profile) : AccountUiState()
    data class Failure(val error: String?) : AccountUiState()
}