package link.jingweih.chatme.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import link.jingweih.chatme.domain.Profile
import link.jingweih.chatme.usecase.GetUserProfileUseCase
import link.jingweih.chatme.usecase.LogoutUseCase
import link.jingweih.jingwei.core.framework.domain.Result
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    getUserProfileUseCase: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _accountUiState = MutableLiveData<AccountUiState>()
    val accountUiState: LiveData<AccountUiState> = _accountUiState

    init {
        getUserProfileUseCase(Unit)
            .onEach {
                _accountUiState.value = AccountUiState.Success(it)
            }
            .catch { e ->
                _accountUiState.value =
                    AccountUiState.Failure(e.message)
            }
            .launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            when (val result = logoutUseCase(Unit)) {
                is Result.Success -> _accountUiState.value = AccountUiState.Logout
                is Result.Error -> _accountUiState.value =
                    AccountUiState.Failure(result.exception.message)
            }
        }
    }
}

sealed class AccountUiState {
    data class Success(val profile: Profile) : AccountUiState()
    data class Failure(val error: String?) : AccountUiState()
    object Logout : AccountUiState()
}