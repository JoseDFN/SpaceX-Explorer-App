package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Payload
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific payload by ID.
 * This encapsulates the business logic for retrieving a single payload.
 */
class GetPayloadByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a payload by ID
     * @param id The ID of the payload to retrieve
     * @return Result containing the payload or an error
     */
    suspend operator fun invoke(id: String): Result<Payload> {
        return repository.getPayloadById(id)
    }
} 