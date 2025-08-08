package com.jdf.spacexexplorer.domain.model

/**
 * Enum class defining all possible sorting options for the SpaceX Explorer app.
 * Each sort option represents a different way to order the data.
 */
enum class SortOption {
    // Date-based sorting
    DATE_DESC,           // Newest first
    DATE_ASC,            // Oldest first
    
    // Name-based sorting
    NAME_ASC,            // Alphabetical A-Z
    NAME_DESC,           // Alphabetical Z-A
    
    // Launch-specific sorting
    FLIGHT_NUMBER_ASC,   // Flight number ascending
    FLIGHT_NUMBER_DESC,  // Flight number descending
    
    // Rocket-specific sorting
    ROCKET_NAME_ASC,     // Rocket name A-Z
    ROCKET_NAME_DESC,    // Rocket name Z-A
    
    // Crew-specific sorting
    CREW_NAME_ASC,       // Crew name A-Z
    CREW_NAME_DESC,      // Crew name Z-A
    
    // Capsule-specific sorting
    CAPSULE_SERIAL_ASC,  // Capsule serial A-Z
    CAPSULE_SERIAL_DESC, // Capsule serial Z-A
    
    // Core-specific sorting
    CORE_SERIAL_ASC,     // Core serial A-Z
    CORE_SERIAL_DESC,    // Core serial Z-A
    
    // Dragon-specific sorting
    DRAGON_NAME_ASC,     // Dragon name A-Z
    DRAGON_NAME_DESC,    // Dragon name Z-A
    
    // Ship-specific sorting
    SHIP_NAME_ASC,       // Ship name A-Z
    SHIP_NAME_DESC,      // Ship name Z-A
    
    // Landpad-specific sorting
    LANDPAD_NAME_ASC,    // Landpad name A-Z
    LANDPAD_NAME_DESC,   // Landpad name Z-A
    
    // Launchpad-specific sorting
    LAUNCHPAD_NAME_ASC,  // Launchpad name A-Z
    LAUNCHPAD_NAME_DESC, // Launchpad name Z-A
    
    // Payload-specific sorting
    PAYLOAD_NAME_ASC,    // Payload name A-Z
    PAYLOAD_NAME_DESC,   // Payload name Z-A
} 