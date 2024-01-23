package com.todolist.endpoints

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class GetStatusRouteTest {
    @Test
    fun `status endpoint should return 200 when server is up and running`() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        val response = client.get("/v1/status")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
