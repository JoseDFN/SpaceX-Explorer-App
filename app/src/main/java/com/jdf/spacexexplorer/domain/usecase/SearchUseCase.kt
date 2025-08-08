package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.SearchResult
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for searching across all SpaceX entities.
 * This encapsulates the business logic for unified search functionality.
 */
class SearchUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for searching across all entities
     * @param query The search query string
     * @return Result containing list of SearchResult items
     */
    suspend operator fun invoke(query: String): Result<List<SearchResult>> {
        return repository.searchAll(query)
    }
}
