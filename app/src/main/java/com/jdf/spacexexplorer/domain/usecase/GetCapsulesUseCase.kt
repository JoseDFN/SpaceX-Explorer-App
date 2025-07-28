package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Capsule
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all capsules.
 * This encapsulates the business logic for retrieving capsule data.
 */
class GetCapsulesUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting capsules
     * @return Flow of Result containing list of capsules or an error
     */
    operator fun invoke(): Flow<Result<List<Capsule>>> {
        return repository.getCapsules()
    }
} 