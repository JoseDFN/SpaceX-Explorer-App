package com.jdf.spacexexplorer.domain.usecase

import com.jdf.spacexexplorer.domain.model.Theme
import com.jdf.spacexexplorer.domain.repository.SpaceXRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: SpaceXRepository
) {
    suspend operator fun invoke(theme: Theme) = repository.setTheme(theme)
}


