package com.todolist.endpoints

import com.todolist.DatabaseFactory
import com.todolist.models.Activity
import com.todolist.models.Frequency
import com.todolist.models.Priority
import com.todolist.utils.testHttpClient
import com.todolist.utils.toDoListTestApplication
import getActivitiesByUser
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.routing.route
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.junit.BeforeClass
import java.time.Instant
import kotlin.test.BeforeTest
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
    ) = toDoListTestApplication {
        block()
    }

    @BeforeTest
    fun emptyDatabase() {
        DatabaseFactory.init("ToDoListDBTest")
    }

    @Test
    fun `post activity endpoint should return 200 when new activity is successfully inserted in db`() =
        postActivityRouteTestApplication {
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivity)
            }
            val responseBody = response.body<String>()

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(mockActivity.userId, responseBody)

            val activitiesByUser: List<Activity> = getActivitiesByUser(mockActivity.userId)

            assertEquals(1, activitiesByUser.size)
            assertEquals(mockActivity, activitiesByUser.first())
        }

    @Test
    fun `post activity endpoint should return 200 when new activity with nullables is successfully inserted in db`() =
        toDoListTestApplication {
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
            val responseBody = response.body<String>()

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(mockActivityWithNullables.userId, responseBody)

            val activitiesByUser: List<Activity> = getActivitiesByUser(mockActivityWithNullables.userId)

            assertEquals(1, activitiesByUser.size)
            assertEquals(mockActivityWithNullables, activitiesByUser.first())
        }

    @Test
    fun `post activity endpoint should return 400 when userId is empty`() =
        toDoListTestApplication {
            val mockInvalidActivity = mockActivity.copy(userId = "")
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Username must not be empty", response.bodyAsText())
        }

    @Test
    fun `post activity endpoint should return 400 when title is empty`() =
        toDoListTestApplication {
            val mockInvalidActivity = mockActivity.copy(title = "")
            val response = testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidActivity)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Title must not be empty", response.bodyAsText())
        }
}