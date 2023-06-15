package dev.aquiladvx.speerandroidassessment.ui

import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.data.network.GithubNetworkErrors

sealed class UserConnectionsState {
    object Loading: UserConnectionsState()
    class Found(val userConnections: List<GithubUserProfile>) : UserConnectionsState()
    class Error(val error: GithubNetworkErrors): UserConnectionsState()
}