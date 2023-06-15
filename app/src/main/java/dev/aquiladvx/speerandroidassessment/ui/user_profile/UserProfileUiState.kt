package dev.aquiladvx.speerandroidassessment.ui.user_profile

import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.data.network.GithubNetworkErrors

sealed class UserProfileUiState {
    object Loading : UserProfileUiState()
    object NotFound : UserProfileUiState()
    class Found(val userProfile: GithubUserProfile) : UserProfileUiState()
    class Error(val error: GithubNetworkErrors) : UserProfileUiState()
}