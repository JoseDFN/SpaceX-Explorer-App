package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Payload
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.model.FilterOption
import com.jdf.spacexexplorer.domain.model.SortOption
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all payloads.
 * This encapsulates the business logic for retrieving payload data.
 */
class GetPayloadsUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    
    /**
     * Single public responsibility - invoke operator for getting all payloads
     * @param filters List of filter options to apply
     * @param sort Sort option to apply
     * @return Flow of Result containing list of payloads or an error
     */
    operator fun invoke(filters: List<FilterOption> = emptyList(), sort: SortOption = SortOption.NAME_ASC): Flow<Result<List<Payload>>> {
        return repository.getPayloads(filters, sort)
    }
} 