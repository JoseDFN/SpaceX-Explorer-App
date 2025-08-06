package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Core
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all cores.
 * This encapsulates the business logic for retrieving core data.
 */
class GetCoresUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting cores
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     * @return Flow of Result containing list of cores or an error
     */
    operator fun invoke(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Core>>> {
        return repository.getCores(filters, sort)
    }
} 