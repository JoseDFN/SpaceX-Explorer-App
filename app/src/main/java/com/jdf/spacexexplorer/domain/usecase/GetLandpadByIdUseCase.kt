package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Landpad
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific landpad by ID.
 * This encapsulates the business logic for retrieving a single landpad.
 */
class GetLandpadByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a landpad by ID
     * @param id The ID of the landpad to retrieve
     * @return Result containing the landpad or an error
     */
    suspend operator fun invoke(id: String): Result<Landpad> {
        return repository.getLandpadById(id)
    }
} 