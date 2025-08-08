package com.jdf.spacexexplorer.presentation.screens.search

import com.jdf.spacexexplorer.domain.model.SearchResult

/**
 * UI state for the Search screen
 */
data class SearchState(
    val searchQuery: String = "",
    val results: List<SearchResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
