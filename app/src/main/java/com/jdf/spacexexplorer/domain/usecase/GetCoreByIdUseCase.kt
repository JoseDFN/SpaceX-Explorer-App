package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific core by ID.
 * This encapsulates the business logic for retrieving a single core.
 */
class GetCoreByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting a core by ID
     * @param id The ID of the core to retrieve
     * @return Result containing the core or an error
     */
    suspend operator fun invoke(id: String): Result<Core> {
        return repository.getCoreById(id)
    }
} 