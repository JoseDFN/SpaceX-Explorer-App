package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting past launches.
 * This encapsulates the business logic for retrieving past launch data.
 */
class GetPastLaunchesUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting past launches
     * @param limit Maximum number of past launches to retrieve
     * @return Flow of Result containing list of past launches
     */
    operator fun invoke(limit: Int = 20): Flow<Result<List<Launch>>> {
        return repository.getPastLaunches(limit)
    }
} 