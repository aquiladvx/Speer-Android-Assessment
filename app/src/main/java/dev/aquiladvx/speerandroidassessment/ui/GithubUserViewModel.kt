package dev.aquiladvx.speerandroidassessment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.aquiladvx.speerandroidassessment.data.repository.GithubUserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserViewModel @Inject constructor(private val repository: GithubUserRepository): ViewModel() {

    private val _userProfile = MutableLiveData<UserProfileState>()
    val userProfile: LiveData<UserProfileState> = _userProfile

    private val _userConnections = MutableLiveData<UserConnectionsState>()
    val userConnections: LiveData<UserConnectionsState> get() = _userConnections

    val username = MutableLiveData("")

    fun getUserProfile(newUsername: String) = viewModelScope.launch {
        _userProfile.value = UserProfileState.Loading
        username.value = newUsername
        val response = repository.fetchUserProfile(newUsername)

        _userProfile.value = response
    }

    fun getUserFollowers() = viewModelScope.launch {
        _userConnections.value = UserConnectionsState.Loading
        val response = repository.fetchUserFollowers(username.value!!)

        _userConnections.value = response
    }

    fun getUserFollowing() = viewModelScope.launch {
        _userConnections.value = UserConnectionsState.Loading
        val response = repository.fetchUserFollowing(username.value!!)

        _userConnections.value = response
    }
}