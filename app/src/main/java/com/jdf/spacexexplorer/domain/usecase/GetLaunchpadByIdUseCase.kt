package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Launchpad
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific launchpad by ID.
 * This encapsulates the business logic for retrieving a single launchpad.
 */
class GetLaunchpadByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a launchpad by ID
     * @param id The ID of the launchpad to retrieve
     * @return Result containing the launchpad or an error
     */
    suspend operator fun invoke(id: String): Result<Launchpad> {
        return repository.getLaunchpadById(id)
    }
} 