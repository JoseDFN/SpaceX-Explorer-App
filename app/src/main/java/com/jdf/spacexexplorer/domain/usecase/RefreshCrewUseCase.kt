package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for refreshing crew data from the network.
 * This encapsulates the business logic for refreshing crew data.
 */
class RefreshCrewUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {

    /**
     * Single public responsibility - invoke operator for refreshing crew data
     * @return Result indicating success or failure of the refresh operation
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshCrew()
    }
} 