package com.jdf.spacexexplorer.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdf.spacexexplorer.domain.model.Result
import com.jdf.spacexexplorer.domain.usecase.GetLaunchesUseCase
import com.jdf.spacexexplorer.domain.usecase.GetRocketsUseCase
import com.jdf.spacexexplorer.domain.usecase.GetCapsulesUseCase
import com.jdf.spacexexplorer.domain.usecase.GetCoresUseCase
import com.jdf.spacexexplorer.domain.usecase.GetCrewUseCase
import com.jdf.spacexexplorer.domain.usecase.GetShipsUseCase
import com.jdf.spacexexplorer.domain.usecase.GetDragonsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshLaunchesUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshRocketsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCapsulesUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCoresUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshCrewUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshShipsUseCase
import com.jdf.spacexexplorer.domain.usecase.RefreshDragonsUseCase
import com.jdf.spacexexplorer.presentation.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen dashboard.
 * Manages the UI state and business logic for displaying the dashboard with multiple sections.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLaunchesUseCase: GetLaunchesUseCase,
    private val getRocketsUseCase: GetRocketsUseCase,
    private val getCapsulesUseCase: GetCapsulesUseCase,
    private val getCoresUseCase: GetCoresUseCase,
    private val getCrewUseCase: GetCrewUseCase,
    private val getShipsUseCase: GetShipsUseCase,
    private val getDragonsUseCase: GetDragonsUseCase,
    private val refreshLaunchesUseCase: RefreshLaunchesUseCase,
    private val refreshRocketsUseCase: RefreshRocketsUseCase,
    private val refreshCapsulesUseCase: RefreshCapsulesUseCase,
    private val refreshCoresUseCase: RefreshCoresUseCase,
    private val refreshCrewUseCase: RefreshCrewUseCase,
    private val refreshShipsUseCase: RefreshShipsUseCase,
    private val refreshDragonsUseCase: RefreshDragonsUseCase
) : ViewModel() {
    
    /**
     * Private mutable state flow for internal state management
     */
    private val _state = MutableStateFlow(HomeState())
    
    /**
     * Public immutable state flow exposed to the UI
     */
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    /**
     * Navigation events channel for one-time navigation events
     */
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()
    
    init {
        // Launch separate coroutines to fetch data for each section
        loadLaunches()
        loadRockets()
        loadCapsules()
        loadCores()
        loadCrew()
        loadShips()
        loadDragons()
    }
    
    /**
     * Handle UI events from the user
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Refresh -> {
                refreshAllData()
            }
            is HomeEvent.Retry -> {
                retry()
            }
            is HomeEvent.DismissError -> {
                clearAllErrors()
            }
            is HomeEvent.LaunchClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToLaunchDetail(event.launch.id))
                }
            }
            is HomeEvent.RocketClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToRocketDetail(event.rocket.id))
                }
            }
            is HomeEvent.CapsuleClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToCapsuleDetail(event.capsule.id))
                }
            }
            is HomeEvent.CoreClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToCoreDetail(event.core.id))
                }
            }
            is HomeEvent.CrewMemberClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToCrewDetail(event.crewMember.id))
                }
            }
            is HomeEvent.ShipClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToShipDetail(event.ship.id))
                }
            }
            is HomeEvent.DragonClicked -> {
                viewModelScope.launch {
                    _navigationEvents.send(NavigationEvent.NavigateToDragonDetail(event.dragon.id))
                }
            }
        }
    }
    
    /**
     * Load launches from the use case
     */
    private fun loadLaunches() {
        viewModelScope.launch {
            getLaunchesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLaunchesLoading = true,
                                launchesError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the latest 10 launches for the dashboard
                        val launches = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isLaunchesLoading = false,
                                launches = launches,
                                launchesError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isLaunchesLoading = false,
                                launchesError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Load rockets from the use case
     */
    private fun loadRockets() {
        viewModelScope.launch {
            getRocketsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isRocketsLoading = true,
                                rocketsError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the first 10 rockets for the dashboard
                        val rockets = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isRocketsLoading = false,
                                rockets = rockets,
                                rocketsError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isRocketsLoading = false,
                                rocketsError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }

                }
            }
        }
    }
    
    /**
     * Load capsules from the use case
     */
    private fun loadCapsules() {
        viewModelScope.launch {
            getCapsulesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isCapsulesLoading = true,
                                capsulesError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the first 10 capsules for the dashboard
                        val capsules = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isCapsulesLoading = false,
                                capsules = capsules,
                                capsulesError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isCapsulesLoading = false,
                                capsulesError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Load cores from the use case
     */
    private fun loadCores() {
        viewModelScope.launch {
            getCoresUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isCoresLoading = true,
                                coresError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the first 10 cores for the dashboard
                        val cores = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isCoresLoading = false,
                                cores = cores,
                                coresError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isCoresLoading = false,
                                coresError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Load crew from the use case
     */
    private fun loadCrew() {
        viewModelScope.launch {
            getCrewUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isCrewLoading = true,
                                crewError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the first 10 crew members for the dashboard
                        val crew = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isCrewLoading = false,
                                crew = crew,
                                crewError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isCrewLoading = false,
                                crewError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Load ships from the use case
     */
    private fun loadShips() {
        viewModelScope.launch {
            getShipsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isShipsLoading = true,
                                shipsError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the first 10 ships for the dashboard
                        val ships = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isShipsLoading = false,
                                ships = ships,
                                shipsError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isShipsLoading = false,
                                shipsError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Load dragons from the use case
     */
    private fun loadDragons() {
        viewModelScope.launch {
            getDragonsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isDragonsLoading = true,
                                dragonsError = null
                            )
                        }
                    }
                    is Result.Success -> {
                        // Take only the first 10 dragons for the dashboard
                        val dragons = result.data.take(10)
                        _state.update { currentState ->
                            currentState.copy(
                                isDragonsLoading = false,
                                dragons = dragons,
                                dragonsError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { currentState ->
                            currentState.copy(
                                isDragonsLoading = false,
                                dragonsError = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Refresh all data from the remote API
     */
    private fun refreshAllData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            // Refresh all sections
            val launchesRefreshResult = refreshLaunchesUseCase()
            val rocketsRefreshResult = refreshRocketsUseCase()
            val capsulesRefreshResult = refreshCapsulesUseCase()
            val coresRefreshResult = refreshCoresUseCase()
            val crewRefreshResult = refreshCrewUseCase()
            val shipsRefreshResult = refreshShipsUseCase()
            val dragonsRefreshResult = refreshDragonsUseCase()
            
            // Check for errors and update state accordingly
            val errors = mutableListOf<Pair<String, String>>()
            
            if (launchesRefreshResult is Result.Error) {
                errors.add("launches" to (launchesRefreshResult.exception.message ?: "Failed to refresh launches"))
            }
            if (rocketsRefreshResult is Result.Error) {
                errors.add("rockets" to (rocketsRefreshResult.exception.message ?: "Failed to refresh rockets"))
            }
            if (capsulesRefreshResult is Result.Error) {
                errors.add("capsules" to (capsulesRefreshResult.exception.message ?: "Failed to refresh capsules"))
            }
            if (coresRefreshResult is Result.Error) {
                errors.add("cores" to (coresRefreshResult.exception.message ?: "Failed to refresh cores"))
            }
            if (crewRefreshResult is Result.Error) {
                errors.add("crew" to (crewRefreshResult.exception.message ?: "Failed to refresh crew"))
            }
            if (shipsRefreshResult is Result.Error) {
                errors.add("ships" to (shipsRefreshResult.exception.message ?: "Failed to refresh ships"))
            }
            if (dragonsRefreshResult is Result.Error) {
                errors.add("dragons" to (dragonsRefreshResult.exception.message ?: "Failed to refresh dragons"))
            }
            
            _state.update { currentState ->
                currentState.copy(
                    isRefreshing = false,
                    launchesError = errors.find { it.first == "launches" }?.second,
                    rocketsError = errors.find { it.first == "rockets" }?.second,
                    capsulesError = errors.find { it.first == "capsules" }?.second,
                    coresError = errors.find { it.first == "cores" }?.second,
                    crewError = errors.find { it.first == "crew" }?.second,
                    shipsError = errors.find { it.first == "ships" }?.second,
                    dragonsError = errors.find { it.first == "dragons" }?.second
                )
            }
        }
    }
    
    /**
     * Clear all error states
     */
    private fun clearAllErrors() {
        _state.update { 
            it.copy(
                launchesError = null,
                rocketsError = null,
                capsulesError = null,
                coresError = null,
                crewError = null,
                shipsError = null,
                dragonsError = null
            )
        }
    }
    
    /**
     * Retry loading all data
     */
    private fun retry() {
        clearAllErrors()
        loadLaunches()
        loadRockets()
        loadCapsules()
        loadCores()
        loadCrew()
        loadShips()
        loadDragons()
    }
} 