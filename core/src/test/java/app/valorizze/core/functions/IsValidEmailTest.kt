package app.valorizze.core.functions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsValidEmailTest {

    @Test
    fun `returns true for valid email`() {
        assertTrue(isValidEmail("john.doe@example.com"))
    }

    @Test
    fun `trims spaces before validating`() {
        assertTrue(isValidEmail("  john.doe@example.com  "))
    }

    @Test
    fun `is case-insensitive`() {
        assertTrue(isValidEmail("JOHN.DOE@EXAMPLE.COM"))
        assertTrue(isValidEmail("John.Doe@Example.Com"))
    }

    @Test
    fun `returns false when missing at sign`() {
        assertFalse(isValidEmail("john.doeexample.com"))
    }

    @Test
    fun `returns false when missing domain`() {
        assertFalse(isValidEmail("john.doe@"))
        assertFalse(isValidEmail("john.doe@example"))
    }

    @Test
    fun `returns false for blank`() {
        assertFalse(isValidEmail(""))
        assertFalse(isValidEmail("   "))
    }
}

