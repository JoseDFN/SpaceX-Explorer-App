package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Ship
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all ships.
 * This encapsulates the business logic for retrieving ship data.
 */
class GetShipsUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {

    /**
     * Single public responsibility - invoke operator for getting ships
     * @return Flow of Result containing list of ships or an error
     */
    operator fun invoke(): Flow<Result<List<Ship>>> {
        return repository.getShips()
    }
} 