package com.smponi.reader.core.designsystem

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidAccessibilityCompatibilityTest {

    @Test
    fun api33NeverLinksTheApi36ContrastReader() {
        val increaseContrast = readHighContrastPreference(
            sdkInt = 33,
            api36Reader = {
                throw NoSuchMethodError("isHighContrastTextEnabled is unavailable before API 36")
            },
            legacyReader = { true },
        )

        assertTrue(increaseContrast)
    }

    @Test
    fun api36UsesTheModernContrastReader() {
        val increaseContrast = readHighContrastPreference(
            sdkInt = 36,
            api36Reader = { true },
            legacyReader = {
                error("The legacy reader must not run on API 36")
            },
        )

        assertTrue(increaseContrast)
    }

    @Test
    fun unavailablePlatformPreferenceFailsClosed() {
        val increaseContrast = readHighContrastPreference(
            sdkInt = 33,
            api36Reader = { true },
            legacyReader = {
                error("Platform accessibility preference is unavailable")
            },
        )

        assertFalse(increaseContrast)
    }
}
