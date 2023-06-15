package dev.aquiladvx.speerandroidassessment.data.entity

import com.google.gson.annotations.SerializedName

data class GithubErrorBody(
    @SerializedName("message")
    val message: String,
)