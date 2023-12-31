package dev.aquiladvx.speerandroidassessment.ui.user_connections

import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import dev.aquiladvx.speerandroidassessment.data.network.GithubNetworkErrors

sealed class UserConnectionsUiState {
    object NotFound : UserConnectionsUiState()
    class Found(val userConnections: List<GithubUserProfile>) : UserConnectionsUiState()
    class Error(val error: GithubNetworkErrors) : UserConnectionsUiState()
    sealed class Loading : UserConnectionsUiState() {
        object FromEmpty : Loading()
        object FromData : Loading()
    }
}