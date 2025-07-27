package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific launch by ID.
 * This encapsulates the business logic for retrieving a single launch.
 */
class GetLaunchByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a launch by ID
     * @param launchId The unique identifier of the launch
     * @return Result containing the launch or null if not found
     */
    suspend operator fun invoke(launchId: String): Result<Launch?> {
        return repository.getLaunchById(launchId)
    }
} 