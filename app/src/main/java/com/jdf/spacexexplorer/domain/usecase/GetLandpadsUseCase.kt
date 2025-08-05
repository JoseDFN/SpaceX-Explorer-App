package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Landpad
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all landpads.
 * This encapsulates the business logic for retrieving landpad data.
 */
class GetLandpadsUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting landpads
     * @return Flow of Result containing list of landpads
     */
    operator fun invoke(): Flow<Result<List<Landpad>>> {
        return repository.getLandpads()
    }
} 