package com.todolist.endpoints

import com.todolist.DatabaseFactory
import com.todolist.models.Activity
import com.todolist.models.Frequency
import com.todolist.models.Priority
import com.todolist.utils.testHttpClient
import com.todolist.utils.toDoListTestApplication
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import org.jetbrains.annotations.Range
import java.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetActivitiesRouteTest {
    private val mockActivity = Activity(
        userId = "e58ed763-928c-4155-bee9-fdbaaadc15f3",
        title = "my test activity",
        dueDate = Instant.parse("2024-01-22T15:39:03.800453Z"),
        frequency = Frequency.ONCE
    )

    private fun getActivitiesRouteTestApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) = toDoListTestApplication {
        block()
    }

    @BeforeTest
    fun emptyDatabase() {
        DatabaseFactory.init("ToDoListDBTest")
    }

    @Test
    fun `get activities endpoint should return 200 and list of activities by user when user ID is valid`() =
        getActivitiesRouteTestApplication {
            repeat(3) {
                testHttpClient().post("/v1/activity") {
                    contentType(ContentType.Application.Json)
                    setBody(mockActivity)
                }
            }

            val userId = "e58ed763-928c-4155-bee9-fdbaaadc15f3"
            val response = testHttpClient().get("/v1/activities/$userId")
            val activitiesByUser: List<Activity> = response.body<List<Activity>>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(3, activitiesByUser.size)
            assertEquals(mockActivity, activitiesByUser.first())
        }

    @Test
    fun `get activities endpoint should return 404 when user ID is empty`() =
        getActivitiesRouteTestApplication {
            val userId = ""
            val response = testHttpClient().get("/v1/activities/$userId/")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    @Test
    fun `get activities endpoint should return 404 when user ID is whitespace`() =
        getActivitiesRouteTestApplication {
            val userId = " "
            val response = testHttpClient().get("/v1/activities/$userId/")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    @Test
    fun `get activities endpoint should return 400 when user ID is invalid`() =
        getActivitiesRouteTestApplication {
            val userId = "invalidUserId"
            val response = testHttpClient().get("/v1/activities/$userId")

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
}