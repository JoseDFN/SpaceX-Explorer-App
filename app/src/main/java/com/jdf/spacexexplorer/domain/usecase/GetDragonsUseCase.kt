package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Dragon
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all dragons.
 * This encapsulates the business logic for retrieving dragon data.
 */
class GetDragonsUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {

    /**
     * Get all dragons as a Flow
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     * @return Flow of Result containing list of dragons
     */
    operator fun invoke(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Dragon>>> {
        return repository.getDragons(filters, sort)
    }
} 