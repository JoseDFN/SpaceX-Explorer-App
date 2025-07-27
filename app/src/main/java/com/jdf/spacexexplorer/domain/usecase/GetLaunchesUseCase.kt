package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all launches.
 * This encapsulates the business logic for retrieving launch data.
 */
class GetLaunchesUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting launches
     * @return Flow of Result containing list of launches
     */
    operator fun invoke(): Flow<Result<List<Launch>>> {
        return repository.getLaunches()
    }
} 