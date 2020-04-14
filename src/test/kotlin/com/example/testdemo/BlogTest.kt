package com.example.testdemo

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.assertj.core.api.Assertions.*
import java.time.LocalDateTime

// Lifecycle per class (also set in junit-platform.properties)
// A single instance of the test class is used for every method.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlogTest {

    // Create mocks once and clear before tests
    private val authorMock: Author = mockk()
    private val dbMock: Database = mockk()

    // Avoid Static and Reuse the Test Class Instance
    private val blog = Blog(dbMock);

    // Reset the mocks
    @BeforeEach
    fun init() {
        clearAllMocks();
    }

    @Test
    fun `add a post`() {
        every { dbMock.insertPost(any(), any(), any(), any()) } just runs;

        every { authorMock.name } returns "y"
        every { authorMock.email } returns mockk {
            every { toEmail() } returns "x"
        }

        blog.addPost(createPost(), authorMock);

        verify {
            dbMock.insertPost("x", "y", "Another Post", LocalDateTime.of(2020, 4, 5, 10, 11, 12))
        }
    }

    @Test
    fun `get a post`() {
        // Static function mock
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.of(2020, 1, 2, 3, 4, 5)

        val actualPost = blog.getPost(id = 1)

        val expectedPost = Post(
                body = "My Post",
                date = LocalDateTime.of(2020, 1, 2, 3, 4, 5)
        )

        // Compare data classes, not values. Data classes implement equals() and toString() out of the box.
        assertThat(actualPost).isEqualTo(expectedPost);
    }

    // Helper function to create objects
    private fun createPost(): Post {
        return Post(
                body = "Another Post",
                date = LocalDateTime.of(2020, 4, 5, 10, 11, 12)
        )
    }

}
