package dev.aquiladvx.speerandroidassessment.ui

import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.data.network.GithubNetworkErrors

sealed class UserProfileState {
    object Loading: UserProfileState()
    object NotFound: UserProfileState()
    class Found(val userProfile: GithubUserProfile) : UserProfileState()
    class Error(val error: GithubNetworkErrors): UserProfileState()
}