package dev.aquiladvx.speerandroidassessment.data.entity

import com.google.gson.annotations.SerializedName

data class GithubUserProfile(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("blog")
    val blog: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("events_url")
    val eventsUrl: String,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("following")
    val following: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("location")
    val location: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("twitter_username")
    val twitterUsername: String,
)