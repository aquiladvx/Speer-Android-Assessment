package dev.aquiladvx.speerandroidassessment.data.repository

import dev.aquiladvx.speerandroidassessment.common.fromJson
import dev.aquiladvx.speerandroidassessment.data.entity.GithubErrorBody
import dev.aquiladvx.speerandroidassessment.data.network.GithubNetworkErrors
import dev.aquiladvx.speerandroidassessment.data.network.GithubServiceApi
import dev.aquiladvx.speerandroidassessment.ui.UserProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject

class GithubUserRepository @Inject constructor(private val api: GithubServiceApi) {

    suspend fun fetchUserProfile(username: String): UserProfileState {
        return withContext(Dispatchers.IO) {
            val apiResponse = api.fetchUserProfile(username)

            if (!apiResponse.isSuccessful || apiResponse.body() == null) {
                val error = getGithubApiError(apiResponse.errorBody())
                return@withContext UserProfileState.Error(error)
            }

            return@withContext UserProfileState.Found(apiResponse.body()!!)
        }
    }

    private fun getGithubApiError(errorBody: ResponseBody?): GithubNetworkErrors {
        val error = fromJson(errorBody?.string(), GithubErrorBody::class.java)
        return when(error.message) {
            GithubNetworkErrors.BAD_CREDENTIALS.message -> {
               GithubNetworkErrors.BAD_CREDENTIALS
            }

            GithubNetworkErrors.NOT_FOUND.message -> {
                GithubNetworkErrors.NOT_FOUND
            }

            else -> {
                GithubNetworkErrors.UNKNOWN
            }
        }
    }
}