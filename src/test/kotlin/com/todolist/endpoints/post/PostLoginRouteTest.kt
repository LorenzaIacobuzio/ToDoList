package com.todolist.endpoints.post

import com.todolist.utils.configureTestApplication
import com.todolist.utils.testHttpClient
import com.todolist.utils.testLogin
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PostLoginRouteTest {
    @Test
    fun `post login endpoint should return 200 when user is successfully logged in`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(testLogin)
            }

            assertEquals(HttpStatusCode.OK, response.status)
            assertTrue { response.bodyAsText().contains("token") }
        }

    @Test
    fun `post login endpoint should return 400 when username is empty`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(testLogin.copy(username = ""))
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Username must not be blank", response.bodyAsText())
        }

    @Test
    fun `post login endpoint should return 400 when username is whitespace`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(testLogin.copy(username = " "))
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Username must not be blank", response.bodyAsText())
        }

    @Test
    fun `post login endpoint should return 400 when password is empty`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(testLogin.copy(password = ""))
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Password must not be blank", response.bodyAsText())
        }

    @Test
    fun `post login endpoint should return 400 when password is whitespace`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/login") {
                contentType(ContentType.Application.Json)
                setBody(testLogin.copy(password = " "))
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Password must not be blank", response.bodyAsText())
        }
}
