package dev.aquiladvx.speerandroidassessment.common

import dev.aquiladvx.speerandroidassessment.common.Constants.GITHUB_PAGE_SIZE
import dev.aquiladvx.speerandroidassessment.ui.user_connections.UserConnectionsUiState

class UserConnectionPaginationController<T> {
    var page: Int = 1
    var currentList = mutableListOf<T>()
    var isLoadingNextPage = false
    var hasLoadedAll = false

    fun loadingState() : UserConnectionsUiState.Loading {
        isLoadingNextPage = true
        return if(currentList.isEmpty())
            UserConnectionsUiState.Loading.FromEmpty
        else
            UserConnectionsUiState.Loading.FromData
    }

    fun canLoadMore() : Boolean{
        if(hasLoadedAll) {
            return false
        }
        if(isLoadingNextPage) {
            return false
        }

        return true
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