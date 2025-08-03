package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for refreshing ships data from the network.
 * This encapsulates the business logic for refreshing ship data.
 */
class RefreshShipsUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshShips()
    }
} 