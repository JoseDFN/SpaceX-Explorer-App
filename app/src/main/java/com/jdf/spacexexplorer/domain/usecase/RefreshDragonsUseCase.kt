package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for refreshing dragons from the remote API.
 * This encapsulates the business logic for refreshing dragon data.
 */
class RefreshDragonsUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for refreshing dragons
     * @return Result indicating success or failure of the refresh operation
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshDragons()
    }
} 