package com.todolist.endpoints.put

import com.todolist.models.Activity
import com.todolist.models.Frequency
import com.todolist.models.Priority
import com.todolist.utils.configureTestApplication
import com.todolist.utils.models.getActivities
import com.todolist.utils.testHttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.time.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class PutActivityRouteTest {
    private val mockActivity = Activity(
        id = UUID.fromString("e58ed763-928c-6666-bee9-fdbaaadc15f3"),
        userId = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"),
        title = "my test activity",
        dueDate = Instant.parse("2024-01-22T15:39:03.800453Z"),
        frequency = Frequency.ONCE
    )

    @Test
    fun `put activity endpoint should return 200 when activity is successfully updated in db`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivity)
            }
            val activities: List<Activity> = getActivities(mockActivity.userId)

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(1, activities.size)
            assertEquals(mockActivity, activities.first())

            val newResponse = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivity.copy(title = "my updated test activity"))
            }
            val newActivities: List<Activity> = getActivities(mockActivity.userId)

            assertEquals(HttpStatusCode.Created, newResponse.status)
            assertEquals(1, newActivities.size)
            assertEquals("my updated test activity", newActivities.first().title)
        }

    @Test
    fun `put activity endpoint should return 200 when activity with nullables is successfully updated in db`() =
        configureTestApplication {
            val mockActivityWithNullables = mockActivity.copy(
                userId = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f4"),
                group = "Personal",
                priority = Priority.LOW,
                description = "Description",
                rescheduledToDate = Instant.parse("2024-01-22T15:39:03.800453Z"),
                frequency = Frequency.WEEKLY
            )
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivityWithNullables)
            }
            val activities: List<Activity> = getActivities(mockActivityWithNullables.userId)

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(1, activities.size)
            assertEquals(mockActivityWithNullables, activities.first())

            val newResponse = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivityWithNullables.copy(title = "my updated test activity"))
            }
            val newActivities: List<Activity> = getActivities(mockActivityWithNullables.userId)

            assertEquals(HttpStatusCode.Created, newResponse.status)
            assertEquals(1, newActivities.size)
            assertEquals("my updated test activity", newActivities.first().title)
        }

    @Test(expected = IllegalArgumentException::class)
    fun `put activity endpoint should return 400 when userId is empty`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = UUID.fromString(""))
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `put activity endpoint should return 400 when userId is whitespace`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = UUID.fromString(" "))
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `put activity endpoint should return 400 when userId is not UUID`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = UUID.fromString("e58ed763-928c-bee9-fdbaaadc15f3"))
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `put activity endpoint should return 400 when id is empty`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString(""))
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `put activity endpoint should return 400 when id is whitespace`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString(" "))
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `put activity endpoint should return 400 when id is not UUID`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString("e58ed763-928c-bee9-fdbaaadc15f3"))
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test
    fun `put activity endpoint should return 400 when title is empty`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(title = "")
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Title must not be empty", response.bodyAsText())
        }

    @Test
    fun `put activity endpoint should return 400 when title is whitespace`() =
        configureTestApplication {
            val mockInvalidActivity = mockActivity.copy(title = " ")
            val response = testHttpClient().put("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Title must not be whitespace", response.bodyAsText())
        }
}
