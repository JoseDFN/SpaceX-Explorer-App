package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Rocket
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all rockets.
 * This encapsulates the business logic for retrieving rocket data.
 */
class GetRocketsUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting rockets
     * @return Flow of Result containing list of rockets
     */
    operator fun invoke(): Flow<Result<List<Rocket>>> {
        return repository.getRockets()
    }
} 