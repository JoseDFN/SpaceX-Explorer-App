package com.jdf.spacexexplorer.domain.model

/**
 * Sealed interface defining all possible filter options for the SpaceX Explorer app.
 * Each filter type is implemented as a data class with specific parameters.
 */
sealed interface FilterOption {
    
    // Launch-specific filters
    data class LaunchYearFilter(val year: Int) : FilterOption
    data class LaunchSuccessFilter(val successful: Boolean) : FilterOption
    data class LaunchUpcomingFilter(val upcoming: Boolean) : FilterOption
    data class LaunchRocketFilter(val rocketId: String) : FilterOption
    data class LaunchDateRangeFilter(val startDate: Long, val endDate: Long) : FilterOption
    
    // Rocket-specific filters
    data class RocketActiveFilter(val active: Boolean) : FilterOption
    data class RocketTypeFilter(val type: String) : FilterOption
    
    // Crew-specific filters
    data class CrewAgencyFilter(val agency: String) : FilterOption
    data class CrewStatusFilter(val status: String) : FilterOption
    
    // Capsule-specific filters
    data class CapsuleTypeFilter(val type: String) : FilterOption
    data class CapsuleStatusFilter(val status: String) : FilterOption
    
    // Core-specific filters
    data class CoreStatusFilter(val status: String) : FilterOption
    data class CoreBlockFilter(val block: Int?) : FilterOption
    
    // Dragon-specific filters
    data class DragonTypeFilter(val type: String) : FilterOption
    data class DragonActiveFilter(val active: Boolean) : FilterOption
    
    // Ship-specific filters
    data class ShipActiveFilter(val active: Boolean) : FilterOption
    data class ShipTypeFilter(val type: String) : FilterOption
    
    // Landpad-specific filters
    data class LandpadStatusFilter(val status: String) : FilterOption
    data class LandpadTypeFilter(val type: String) : FilterOption
    
    // Launchpad-specific filters
    data class LaunchpadStatusFilter(val status: String) : FilterOption
    data class LaunchpadRegionFilter(val region: String) : FilterOption
    
    // Payload-specific filters
    data class PayloadTypeFilter(val type: String) : FilterOption
    data class PayloadNationalityFilter(val nationality: String) : FilterOption
} 