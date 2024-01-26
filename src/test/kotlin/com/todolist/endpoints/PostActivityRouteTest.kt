package com.todolist.endpoints

import com.todolist.models.Activity
import com.todolist.models.Frequency
import com.todolist.models.Priority
import com.todolist.plugins.configureRouting
import com.todolist.utils.DatabaseFactory
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
import kotlin.test.Test
import kotlin.test.assertEquals

class PostActivityRouteTest {
    private val mockActivity = Activity(
        userId = "e58ed763-928c-4155-bee9-fdbaaadc15f3",
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
            DatabaseFactory.init(environment.config)
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
            val activitiesByUser: List<Activity> = getActivitiesByUser(mockActivity.userId)

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals("Activity added to list", response.bodyAsText())
            assertEquals(1, activitiesByUser.size)
            assertEquals(mockActivity, activitiesByUser.first())
        }

    @Test
    fun `post activity endpoint should return 200 when new activity with nullables is successfully inserted in db`() =
        postActivityRouteTestApplication {
            val mockActivityWithNullables = mockActivity.copy(
                userId = "e58ed763-928c-4155-bee9-fdbaaadc15f4",
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
            val activitiesByUser: List<Activity> = getActivitiesByUser(mockActivityWithNullables.userId)

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals("Activity added to list", response.bodyAsText())
            assertEquals(1, activitiesByUser.size)
            assertEquals(mockActivityWithNullables, activitiesByUser.first())
        }

    @Test
    fun `post activity endpoint should return 400 when userId is empty`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = "")
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Username must not be empty", response.bodyAsText())
        }

    @Test
    fun `post activity endpoint should return 400 when userId is whitespace`() =
        postActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = " ")
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Username must not be whitespace", response.bodyAsText())
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
