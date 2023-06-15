package dev.aquiladvx.speerandroidassessment.ui.user_connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.aquiladvx.speerandroidassessment.data.repository.GithubUserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserConnectionsViewModel @Inject constructor(private val repository: GithubUserRepository): ViewModel() {

    private val _userConnections = MutableLiveData<UserConnectionsUiState>()
    val userConnections: LiveData<UserConnectionsUiState> get() = _userConnections

    fun getUserFollowers(username: String) = viewModelScope.launch {
        _userConnections.value = UserConnectionsUiState.Loading
        val response = repository.fetchUserFollowers(username)

        _userConnections.value = response
    }

    fun getUserFollowing(username: String) = viewModelScope.launch {
        _userConnections.value = UserConnectionsUiState.Loading
        val response = repository.fetchUserFollowing(username)

        _userConnections.value = response
    }
}