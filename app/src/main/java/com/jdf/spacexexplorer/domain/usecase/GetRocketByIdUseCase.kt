package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific rocket by ID.
 * This encapsulates the business logic for retrieving a single rocket.
 */
class GetRocketByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a rocket by ID
     * @param id The ID of the rocket to retrieve
     * @return Result containing the rocket or an error
     */
    suspend operator fun invoke(id: String): Result<Rocket> {
        return repository.getRocketById(id)
    }
} 