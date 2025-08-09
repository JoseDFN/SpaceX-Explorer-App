package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case to clear all local caches (Room tables)
 */
class ClearCacheUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    suspend operator fun invoke() {
        repository.clearAllCaches()
    }
}


