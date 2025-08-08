package com.jdf.spacexexplorer.domain.model

/**
 * Sealed interface representing unified search results across all SpaceX entities.
 * This allows a single list to hold different types of search results.
 */
sealed interface SearchResult {
    
    /**
     * Search result for a Launch entity
     */
    data class LaunchResult(val launch: Launch) : SearchResult
    
    /**
     * Search result for a Rocket entity
     */
    data class RocketResult(val rocket: Rocket) : SearchResult
    
    /**
     * Search result for a Capsule entity
     */
    data class CapsuleResult(val capsule: Capsule) : SearchResult
    
    /**
     * Search result for a Core entity
     */
    data class CoreResult(val core: Core) : SearchResult
    
    /**
     * Search result for a CrewMember entity
     */
    data class CrewResult(val crewMember: CrewMember) : SearchResult
    
    /**
     * Search result for a Ship entity
     */
    data class ShipResult(val ship: Ship) : SearchResult
    
    /**
     * Search result for a Dragon entity
     */
    data class DragonResult(val dragon: Dragon) : SearchResult
    
    /**
     * Search result for a Landpad entity
     */
    data class LandpadResult(val landpad: Landpad) : SearchResult
    
    /**
     * Search result for a Launchpad entity
     */
    data class LaunchpadResult(val launchpad: Launchpad) : SearchResult
    
    /**
     * Search result for a Payload entity
     */
    data class PayloadResult(val payload: Payload) : SearchResult
}
