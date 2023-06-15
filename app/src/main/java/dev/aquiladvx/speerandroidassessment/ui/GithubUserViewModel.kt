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

    fun getUserProfile(username: String) = viewModelScope.launch {
        _userProfile.value = UserProfileState.Loading
        val response = repository.fetchUserProfile(username)

        _userProfile.value = response
    }
}