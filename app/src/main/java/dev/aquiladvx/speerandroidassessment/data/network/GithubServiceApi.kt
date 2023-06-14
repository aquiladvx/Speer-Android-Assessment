package dev.aquiladvx.speerandroidassessment.data.network

import dev.aquiladvx.speerandroidassessment.data.entity.GithubUserProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubServiceApi {

    @GET("/users/{username}")
    fun getUserProfile(@Path("username") userName: String): Response<GithubUserProfile>

    @GET("/users/{username}/followers")
    fun getUserFollowers(@Path("username") userName: String): Response<List<GithubUserProfile>>

    @GET("/users/{username}/following")
    fun getUserFollowing(@Path("username") userName: String): Response<List<GithubUserProfile>>
}