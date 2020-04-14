package com.example.testdemo

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.assertj.core.api.Assertions.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailTest {

    // A nested test to group logic
    @Nested
    inner class Validation {

        // Backticks for readable tests / avoids the need for @DisplayName
        @Test
        fun `is valid`() {
            val email = Email("user", "domain.com")
            assertThat(email.isValid()).isTrue()
        }

        @Test
        fun `is not valid`() {
            val email = Email("", "domain.com")
            assertThat(email.isValid()).isFalse()
        }
    }

    @Test
    fun `toEmail`() {
        val email = Email("user", "example.com");
        assertThat(email.toEmail()).isEqualTo("user@example.com")
    }
}
