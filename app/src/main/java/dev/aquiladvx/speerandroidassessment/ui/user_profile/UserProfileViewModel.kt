package dev.aquiladvx.speerandroidassessment.ui.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.aquiladvx.speerandroidassessment.data.repository.GithubUserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val repository: GithubUserRepository) :
    ViewModel() {

    private val _userProfile = MutableLiveData<UserProfileUiState>()
    val userProfile: LiveData<UserProfileUiState> = _userProfile

    val username = MutableLiveData("")

    fun getUserProfile(newUsername: String) = viewModelScope.launch {
        _userProfile.value = UserProfileUiState.Loading
        username.value = newUsername
        val response = repository.fetchUserProfile(newUsername)

        _userProfile.value = response
    }

    fun refreshUserProfile() = viewModelScope.launch {
        username.value?.let {
            getUserProfile(it)
        }
    }


}