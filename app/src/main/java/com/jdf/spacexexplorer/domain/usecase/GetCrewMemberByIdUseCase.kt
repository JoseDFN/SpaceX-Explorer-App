package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.CrewMember
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

/**
 * Use case for getting a specific crew member by ID.
 * This encapsulates the business logic for retrieving a single crew member.
 */
class GetCrewMemberByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {

    /**
     * Single public responsibility - invoke operator for getting a crew member by ID
     * @param id The ID of the crew member to retrieve
     * @return Result containing the crew member or an error
     */
    suspend operator fun invoke(id: String): Result<CrewMember> {
        return repository.getCrewById(id)
    }
} 