package com.jdf.spacexexplorer.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jdf.spacexexplorer.domain.model.Launch
import com.jdf.spacexexplorer.domain.model.Rocket

/**
 * Preview for the HomeScreen with sample data
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Create sample data for preview
    val sampleLaunches = listOf(
        Launch(
            id = "1",
            missionName = "Starlink 4-1",
            launchDate = "2024-01-15T10:30:00Z",
            launchDateUnix = 1705312200,
            wasSuccessful = true,
            isUpcoming = false,
            patchImageUrl = "https://images2.imgbox.com/40/e3/GypSkayF_o.png",
            details = "A SpaceX Falcon 9 rocket launched Starlink satellites",
            rocketId = "falcon9",
            flightNumber = 1,
            webcastUrl = "https://www.youtube.com/watch?v=example",
            articleUrl = "https://spaceflightnow.com/example",
            wikipediaUrl = "https://en.wikipedia.org/wiki/example"
        ),
        Launch(
            id = "2",
            missionName = "Crew-8",
            launchDate = "2024-02-20T15:45:00Z",
            launchDateUnix = 1708443900,
            wasSuccessful = null,
            isUpcoming = true,
            patchImageUrl = "https://images2.imgbox.com/40/e3/GypSkayF_o.png",
            details = "SpaceX Crew-8 mission to the International Space Station",
            rocketId = "falcon9",
            flightNumber = 2,
            webcastUrl = null,
            articleUrl = null,
            wikipediaUrl = null
        )
    )
    
    val sampleRockets = listOf(
        Rocket(
            id = "falcon9",
            name = "Falcon 9",
            type = "rocket",
            description = "A two-stage rocket designed and manufactured by SpaceX",
            height = 70.0,
            mass = 549054.0,
            active = true,
            stages = 2,
            boosters = 0,
            costPerLaunch = 6700000,
            successRatePct = 98,
            firstFlight = "2010-06-04",
            country = "United States",
            company = "SpaceX",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Falcon_9",
            flickrImages = listOf(
                "https://images.unsplash.com/photo-1541185933-ef5d8ed016c2?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
            )
        ),
        Rocket(
            id = "falconheavy",
            name = "Falcon Heavy",
            type = "rocket",
            description = "A super heavy-lift launch vehicle",
            height = 70.0,
            mass = 1420788.0,
            active = true,
            stages = 2,
            boosters = 2,
            costPerLaunch = 97000000,
            successRatePct = 100,
            firstFlight = "2018-02-06",
            country = "United States",
            company = "SpaceX",
            wikipediaUrl = "https://en.wikipedia.org/wiki/Falcon_Heavy",
            flickrImages = listOf(
                "https://images.unsplash.com/photo-1541185933-ef5d8ed016c2?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80"
            )
        )
    )
    
    // Note: This is just a preview - in the actual app, the HomeScreen will use the HomeViewModel
    // and observe the state from it. This preview shows the UI structure with sample data.
} 