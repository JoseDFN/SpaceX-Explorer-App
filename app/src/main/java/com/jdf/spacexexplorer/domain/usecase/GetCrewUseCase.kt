package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all crew members.
 * This encapsulates the business logic for retrieving crew data.
 */
class GetCrewUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting crew members
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     * @return Flow of Result containing list of crew members or an error
     */
    operator fun invoke(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<CrewMember>>> {
        return repository.getCrew(filters, sort)
    }
} 