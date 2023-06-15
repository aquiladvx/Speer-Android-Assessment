package dev.aquiladvx.speerandroidassessment.data.repository

import dev.aquiladvx.speerandroidassessment.common.fromJson
import dev.aquiladvx.speerandroidassessment.data.entity.GithubErrorBody
import dev.aquiladvx.speerandroidassessment.data.network.GithubNetworkErrors
import dev.aquiladvx.speerandroidassessment.data.network.GithubServiceApi
import dev.aquiladvx.speerandroidassessment.ui.user_connections.UserConnectionsUiState
import dev.aquiladvx.speerandroidassessment.ui.user_profile.UserProfileUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.net.UnknownHostException
import javax.inject.Inject

class GithubUserRepository @Inject constructor(private val api: GithubServiceApi) {

    suspend fun fetchUserProfile(username: String): UserProfileUiState {
        return withContext(Dispatchers.IO) {
            try {

                val apiResponse = api.fetchUserProfile(username)

                if (!apiResponse.isSuccessful || apiResponse.body() == null) {
                    val error = getGithubApiError(apiResponse.errorBody())
                    if (error == GithubNetworkErrors.NOT_FOUND) {
                        return@withContext UserProfileUiState.NotFound
                    } else {
                        return@withContext UserProfileUiState.Error(error)
                    }
                }

                return@withContext UserProfileUiState.Found(apiResponse.body()!!)
            } catch (e: UnknownHostException) {
                return@withContext UserProfileUiState.Error(GithubNetworkErrors.UNKNOWN_HOST)
            } catch (e: Exception) {
                return@withContext UserProfileUiState.Error(GithubNetworkErrors.UNKNOWN_ERROR)
            }
        }
    }


    suspend fun fetchUserFollowers(username: String, page: Int): UserConnectionsUiState {
        return withContext(Dispatchers.IO) {
            try {

                val apiResponse = api.fetchUserFollowers(username, page)

                if (!apiResponse.isSuccessful || apiResponse.body() == null) {
                    val error = getGithubApiError(apiResponse.errorBody())
                    return@withContext UserConnectionsUiState.Error(error)
                }

                return@withContext UserConnectionsUiState.Found(apiResponse.body()!!)
            } catch (e: UnknownHostException) {
                return@withContext UserConnectionsUiState.Error(GithubNetworkErrors.UNKNOWN_HOST)
            } catch (e: Exception) {
                return@withContext UserConnectionsUiState.Error(GithubNetworkErrors.UNKNOWN_ERROR)
            }
        }
    }

    suspend fun fetchUserFollowing(username: String, page: Int): UserConnectionsUiState {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = api.fetchUserFollowing(username, page)

                if (!apiResponse.isSuccessful || apiResponse.body() == null) {
                    val error = getGithubApiError(apiResponse.errorBody())
                    return@withContext UserConnectionsUiState.Error(error)
                }

                return@withContext UserConnectionsUiState.Found(apiResponse.body()!!)
            } catch (e: UnknownHostException) {
                return@withContext UserConnectionsUiState.Error(GithubNetworkErrors.UNKNOWN_HOST)
            } catch (e: Exception) {
                return@withContext UserConnectionsUiState.Error(GithubNetworkErrors.UNKNOWN_ERROR)
            }
        }
    }

    private fun getGithubApiError(errorBody: ResponseBody?): GithubNetworkErrors {
        val error = fromJson(errorBody?.string(), GithubErrorBody::class.java)
        return when (error.message) {
            GithubNetworkErrors.BAD_CREDENTIALS.responseMessage -> {
                GithubNetworkErrors.BAD_CREDENTIALS
            }

            GithubNetworkErrors.NOT_FOUND.responseMessage -> {
                GithubNetworkErrors.NOT_FOUND
            }

            else -> {
                GithubNetworkErrors.UNKNOWN_ERROR
            }
        }
    }
}