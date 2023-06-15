package dev.aquiladvx.speerandroidassessment.ui.user_connections

import dev.aquiladvx.speerandroidassessment.common.Constants.GITHUB_PAGE_SIZE

class UserConnectionPaginationController<T> {
    var page: Int = 1
    var currentList = mutableListOf<T>()
    var isLoadingNextPage = false
    var hasLoadedAll = false

    fun loadingState(): UserConnectionsUiState.Loading {
        isLoadingNextPage = true
        return if (currentList.isEmpty())
            UserConnectionsUiState.Loading.FromEmpty
        else
            UserConnectionsUiState.Loading.FromData
    }

    fun canLoadMore(): Boolean {
        if (hasLoadedAll) {
            return false
        }
        return !isLoadingNextPage
    }

    fun reset() {
        page = 1
        currentList.clear()
        isLoadingNextPage = false
        hasLoadedAll = false
    }

    fun addItems(newItems: List<T>) {
        currentList.addAll(newItems)

        if (newItems.size < GITHUB_PAGE_SIZE) {
            hasLoadedAll = true
        }
    }

}