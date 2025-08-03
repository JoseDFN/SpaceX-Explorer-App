package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Ship
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific ship by ID.
 * This encapsulates the business logic for retrieving a single ship.
 */
class GetShipByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    suspend operator fun invoke(id: String): Result<Ship> {
        return repository.getShipById(id)
    }
} 