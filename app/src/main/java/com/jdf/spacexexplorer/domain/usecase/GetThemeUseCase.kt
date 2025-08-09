package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Theme
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    operator fun invoke(): Flow<Theme> = repository.getTheme()
}


