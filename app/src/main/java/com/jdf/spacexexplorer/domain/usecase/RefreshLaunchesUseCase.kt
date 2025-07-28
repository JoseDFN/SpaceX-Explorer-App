package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for refreshing launches from the remote API.
 * This encapsulates the business logic for syncing launch data.
 */
class RefreshLaunchesUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for refreshing launches
     * @return Result indicating success or failure of the refresh operation
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshLaunches()
    }
} 