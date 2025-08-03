package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting launches with pagination support.
 * This encapsulates the business logic for retrieving paginated launch data.
 */
class GetLaunchesPageUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Get a specific page of launches
     * @param page The page number (0-based)
     * @param limit The number of items per page
     * @return Result containing list of launches for the requested page
     */
    suspend operator fun invoke(page: Int, limit: Int = 20): Result<List<Launch>> {
        return repository.getLaunchesPage(page, limit)
    }
} 