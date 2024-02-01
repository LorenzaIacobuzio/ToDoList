package com.todolist.endpoints.get

import com.todolist.models.Activity
import com.todolist.models.Frequency
import com.todolist.utils.configureTestApplication
import com.todolist.utils.testHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.time.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class GetActivityRouteTest {
    private val mockActivity = Activity(
        id = UUID.fromString("e58ed763-928c-6666-bee9-fdbaaadc15f3"),
        userId = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"),
        title = "my test activity",
        dueDate = Instant.parse("2024-01-22T15:39:03.800453Z"),
        frequency = Frequency.ONCE
    )

/*
    private fun getActivityRouteTestApplication(
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
            val jwtService = JwtService(this)
            configureDatabase(environment.config, true)
            configureRouting(jwtService)
        }

        block()
    }
*/
    @Test
    fun `get activity endpoint should return 200 and activity when ID is valid`() =
        configureTestApplication {
            testHttpClient().post("/v1/activity") {
                contentType(ContentType.Application.Json)
                setBody(mockActivity)
            }

            val id = "e58ed763-928c-6666-bee9-fdbaaadc15f3"
            val response = testHttpClient().get("/v1/activity/$id")
            val activity: Activity = response.body<Activity>()

            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(mockActivity, activity)
        }

    @Test
    fun `get activity endpoint should return 404 when ID is empty`() =
        getActivityRouteTestApplication {
            val id = ""
            val response = testHttpClient().get("/v1/activities/$id/")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    @Test
    fun `get activity endpoint should return 404 when ID is whitespace`() =
        getActivityRouteTestApplication {
            val id = " "
            val response = testHttpClient().get("/v1/activities/$id/")

            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    @Test
    fun `get activity endpoint should return 400 when ID is invalid`() =
        getActivityRouteTestApplication {
            val id = "invalidId"
            val response = testHttpClient().get("/v1/activities/$id")

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
}
