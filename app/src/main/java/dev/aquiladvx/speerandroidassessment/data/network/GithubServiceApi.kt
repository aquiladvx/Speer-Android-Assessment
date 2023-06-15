package dev.aquiladvx.speerandroidassessment.data.network

import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubServiceApi {

    @GET("/users/{username}")
    suspend fun fetchUserProfile(@Path("username") userName: String): Response<GithubUserProfile>

    @GET("/users/{username}/followers")
    suspend fun fetchUserFollowers(@Path("username") userName: String): Response<List<GithubUserProfile>>

    @GET("/users/{username}/following")
    suspend fun fetchUserFollowing(@Path("username") userName: String): Response<List<GithubUserProfile>>
}