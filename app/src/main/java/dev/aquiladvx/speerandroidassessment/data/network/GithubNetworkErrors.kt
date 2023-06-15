package dev.aquiladvx.speerandroidassessment.data.network

import androidx.annotation.StringRes
import dev.aquiladvx.speerandroidassessment.R

enum class GithubNetworkErrors(
    val responseMessage: String,
    @StringRes val userMessageId: Int? = null
) {
    BAD_CREDENTIALS("Bad credentials", R.string.bad_credentials_message),
    NOT_FOUND("Not Found"),
    UNKNOWN_HOST("Unknown host", R.string.unknown_host_message),
    UNKNOWN_ERROR("Unknown error", R.string.unknown_error_message)
}