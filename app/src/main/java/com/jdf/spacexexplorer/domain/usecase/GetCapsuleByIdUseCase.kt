package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific capsule by ID.
 * This encapsulates the business logic for retrieving a single capsule.
 */
class GetCapsuleByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a capsule by ID
     * @param id The ID of the capsule to retrieve
     * @return Result containing the capsule or an error
     */
    suspend operator fun invoke(id: String): Result<Capsule> {
        return repository.getCapsuleById(id)
    }
} 