package com.todolist.endpoints

import com.todolist.models.Activity
import com.todolist.models.Frequency
import com.todolist.plugins.configureRouting
import com.todolist.utils.configureDatabase
import com.todolist.utils.getActivities
import com.todolist.utils.testHttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
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

class DeleteActivityRouteTest {
    private val mockActivity = Activity(
        id = UUID.fromString("e58ed763-928c-6666-bee9-fdbaaadc15f3"),
        userId = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"),
        title = "my test activity",
        dueDate = Instant.parse("2024-01-22T15:39:03.800453Z"),
        frequency = Frequency.ONCE
    )

    private fun deleteActivityRouteTestApplication(
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
    fun `delete activity endpoint should return 200 when activity is successfully deleted in db`() =
        deleteActivityRouteTestApplication {
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivity)
            }
            val activities: List<Activity> = getActivities(mockActivity.userId)

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(1, activities.size)
            assertEquals(mockActivity, activities.first())

            val id = "e58ed763-928c-6666-bee9-fdbaaadc15f3"
            val newResponse = testHttpClient().delete("/v1/activity/$id")
            val newActivities: List<Activity> = getActivities(mockActivity.userId)

            assertEquals(HttpStatusCode.OK, newResponse.status)
            assertEquals("Activity deleted", newResponse.bodyAsText())
            assertEquals(0, newActivities.size)
        }

    @Test(expected = IllegalArgumentException::class)
    fun `delete activity endpoint should return 400 when id is empty`() =
        deleteActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString(""))
            val response = testHttpClient().delete("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `delete activity endpoint should return 400 when id is whitespace`() =
        deleteActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString(" "))
            val response = testHttpClient().delete("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `delete activity endpoint should return 400 when id is not UUID`() =
        deleteActivityRouteTestApplication {
            val mockInvalidActivity = mockActivity.copy(id = UUID.fromString("e58ed763-928c-bee9-fdbaaadc15f3"))
            val response = testHttpClient().delete("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }
}
