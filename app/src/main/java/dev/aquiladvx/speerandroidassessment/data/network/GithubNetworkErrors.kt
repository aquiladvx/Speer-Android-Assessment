package dev.aquiladvx.speerandroidassessment.data.network

enum class GithubNetworkErrors(val message: String) {
    BAD_CREDENTIALS("Bad credentials"),
    NOT_FOUND("Not Found"),
    UNKNOWN("")
}