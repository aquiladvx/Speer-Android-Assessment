package dev.aquiladvx.speerandroidassessment.ui.user_connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.data.repository.GithubUserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserConnectionsViewModel @Inject constructor(private val repository: GithubUserRepository) :
    ViewModel() {

    private val connectionsController = UserConnectionPaginationController<GithubUserProfile>()

    private val _userConnections = MutableLiveData<UserConnectionsUiState>()
    val userConnections: LiveData<UserConnectionsUiState> get() = _userConnections

    private suspend fun getUserConnections(connectionsCall: suspend (Int) -> UserConnectionsUiState) {
        if (connectionsController.canLoadMore()) {
            _userConnections.value = connectionsController.loadingState()
            val response = connectionsCall(connectionsController.page)

            if (response !is UserConnectionsUiState.Found) {
                _userConnections.value = response
                return
            }

            connectionsController.page++

            if (response.userConnections.isEmpty()) {
                connectionsController.hasLoadedAll = true
            }

            connectionsController.addItems(response.userConnections)

            if (connectionsController.currentList.isEmpty()) {
                _userConnections.value = UserConnectionsUiState.NotFound
                return
            }

            _userConnections.value =
                UserConnectionsUiState.Found(connectionsController.currentList)

            connectionsController.isLoadingNextPage = false
        }
    }

    fun getUserFollowers(username: String, reset: Boolean = false) = viewModelScope.launch {
        if (reset) {
            connectionsController.reset()
        }
        getUserConnections { page -> repository.fetchUserFollowers(username, page) }

    }

    fun getUserFollowing(username: String, reset: Boolean = false) = viewModelScope.launch {
        if (reset) {
            connectionsController.reset()
        }
        getUserConnections { page -> repository.fetchUserFollowing(username, page) }
    }
}