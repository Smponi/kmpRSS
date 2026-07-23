package com.smponi.reader.feature.home

import kotlin.test.Test
import kotlin.test.assertEquals

class HomeTest {
    @Test
    fun `empty home offers an honest website-following handoff`() {
        val home = openHome()

        assertEquals(HomeState.Empty, home.state)
        assertEquals(
            HomeOutcome.FollowWebsite,
            home.onAction(HomeAction.FollowWebsite),
        )
    }
}
