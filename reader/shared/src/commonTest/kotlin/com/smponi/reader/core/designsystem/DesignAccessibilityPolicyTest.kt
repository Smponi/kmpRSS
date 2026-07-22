package com.smponi.reader.core.designsystem

import kotlin.test.Test
import kotlin.test.assertEquals

class DesignAccessibilityPolicyTest {
    @Test
    fun `system motion preference disables decorative movement`() {
        val policy = DesignAccessibilityPolicy.from(
            AccessibilityPreferences(reduceMotion = true),
        )

        assertEquals(MotionLevel.Reduced, policy.motionLevel)
    }

    @Test
    fun `system contrast preference selects high contrast palette`() {
        val policy = DesignAccessibilityPolicy.from(
            AccessibilityPreferences(increaseContrast = true),
        )

        assertEquals(ContrastLevel.High, policy.contrastLevel)
    }

    @Test
    fun `minimum interactive target is platform safe`() {
        assertEquals(48f, FoundationSize.minimumInteractiveTarget.value)
    }
}
