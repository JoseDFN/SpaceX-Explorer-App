package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

class GetLaunchByIdUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    suspend operator fun invoke(id: String): Result<Launch> = repository.getLaunchById(id)
} 