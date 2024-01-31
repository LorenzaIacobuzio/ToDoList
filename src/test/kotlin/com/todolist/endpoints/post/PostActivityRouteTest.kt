package com.todolist.endpoints.post

import com.todolist.models.Activity
import com.todolist.models.Frequency
import com.todolist.models.Priority
import com.todolist.plugins.configureRouting
import com.todolist.utils.database.configureDatabase
import com.todolist.utils.models.getActivities
import com.todolist.utils.testHttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import java.time.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class PostActivityRouteTest {
    private val mockActivity = Activity(
        id = UUID.fromString("e58ed763-928c-6666-bee9-fdbaaadc15f3"),
        userId = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"),
        title = "my test activity",
        dueDate = Instant.parse("2024-01-22T15:39:03.800453Z"),
        frequency = Frequency.ONCE
    )

    private fun postActivityRouteTestApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) = testApplication {
        createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureDatabase(environment.config, true)
            configureRouting()
        }

        block()
    }

    @Test
    fun `post activity endpoint should return 200 when new activity is successfully inserted in db`() =
        postActivityRouteTestApplication {
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivity)
            }
            val activities: List<Activity> = getActivities(mockActivity.userId)

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals("Activity added to list", response.bodyAsText())
            assertEquals(1, activities.size)
            assertEquals(mockActivity, activities.first())
        }

    @Test
    fun `post activity endpoint should return 200 when new activity with nullables is successfully inserted in db`() =
        postActivityRouteTestApplication {
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
            assertEquals("Activity added to list", response.bodyAsText())
            assertEquals(1, activities.size)
            assertEquals(mockActivityWithNullables, activities.first())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post activity endpoint should return 400 when userId is empty`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = UUID.fromString(""))
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post activity endpoint should return 400 when userId is whitespace`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = UUID.fromString(" "))
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post activity endpoint should return 400 when userId is not UUID`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = UUID.fromString("e58ed763-928c-bee9-fdbaaadc15f3"))
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post activity endpoint should return 400 when id is empty`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString(""))
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post activity endpoint should return 400 when id is whitespace`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString(" "))
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post activity endpoint should return 400 when id is not UUID`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString("e58ed763-928c-bee9-fdbaaadc15f3"))
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test
    fun `post activity endpoint should return 400 when title is empty`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(title = "")
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Title must not be empty", response.bodyAsText())
        }

    @Test
    fun `post activity endpoint should return 400 when title is whitespace`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(title = " ")
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Title must not be whitespace", response.bodyAsText())
        }
}