package com.example.testdemo

import io.mockk.*
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DatabaseTest {

    // Annotation-style declaration
    @SpyK
    private lateinit var database: Database;

    @BeforeEach
    fun init() {
        database = spyk(Database(), recordPrivateCalls = true);
    }

    @Test
    fun `insert post`() {

        // Cast to Unit is needed as the function is private and without the just infix function
        // is not available.
        every { database["execute"](any<String>()) as Unit } just runs

        every { database.wipe() } just runs

        // Named arguments for readability
        database.insertPost(
                email = "my@email.com",
                name = "John Smith",
                body = "Some amazing content",
                date = LocalDateTime.of(2020, 1, 1, 10, 20, 30)
        )

        verify {
            // Verify a private call (needs recordPrivateCalls)
            // Verify the value called (nasty example)
            database["execute"]("INSERT INTO `posts` (`email`,`name`,`body`,`date`) VALUES ('my@email.com', 'John Smith', 'Some amazing content', '2020-01-01T10:20:30');")
        }

        // Verify that this did not happen!
        verify(exactly = 0) {
            database.wipe();
        }
    }

}
