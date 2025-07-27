package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting upcoming launches.
 * This encapsulates the business logic for retrieving upcoming launch data.
 */
class GetUpcomingLaunchesUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting upcoming launches
     * @return Flow of Result containing list of upcoming launches
     */
    operator fun invoke(): Flow<Result<List<Launch>>> {
        return repository.getUpcomingLaunches()
    }
} 