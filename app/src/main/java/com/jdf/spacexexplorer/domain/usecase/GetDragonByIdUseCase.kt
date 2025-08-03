package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Dragon
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific dragon by ID.
 * This encapsulates the business logic for retrieving a single dragon.
 */
class GetDragonByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a dragon by ID
     * @param id The ID of the dragon to retrieve
     * @return Result containing the dragon or an error
     */
    suspend operator fun invoke(id: String): Result<Dragon> {
        return repository.getDragonById(id)
    }
} 