package com.todolist.endpoints.post

import com.todolist.models.User
import com.todolist.utils.configureTestApplication
import com.todolist.utils.models.getUsers
import com.todolist.utils.testHttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class PostUserRouteTest {
    private val mockUser = User(
        userId = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"),
        username = "Lorenza",
        password = "lorenza"
    )

    @Test
    fun `post user endpoint should return 200 when new user is successfully inserted in db`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockUser)
            }
            val users: List<User> = getUsers()

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals("User created", response.bodyAsText())
            assertEquals(2, users.size)
        }

    @Test
    fun `post user endpoint should return 409 when user with same user ID already exists in db`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockUser)
            }
            val users: List<User> = getUsers()

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(2, users.size)

            val duplicateUser = mockUser.copy(username = "new username")
            val newResponse = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(duplicateUser)
            }

            assertEquals(HttpStatusCode.Conflict, newResponse.status)
            assertEquals("User already exists", newResponse.bodyAsText())
        }

    @Test
    fun `post user endpoint should return 409 when user with same username already exists in db`() =
        configureTestApplication {
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockUser)
            }
            val users: List<User> = getUsers()

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(2, users.size)

            val duplicateUser = mockUser.copy(userId = UUID.fromString("e58ed763-928c-0011-bee9-fdbaaadc15f3"))
            val newResponse = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(duplicateUser)
            }

            assertEquals(HttpStatusCode.Conflict, newResponse.status)
            assertEquals("User already exists", newResponse.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post user endpoint should return 400 when userId is empty`() =
        configureTestApplication {
            val mockInvalidUser = mockUser.copy(userId = UUID.fromString(""))
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidUser)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post user endpoint should return 400 when userId is whitespace`() =
        configureTestApplication {
            val mockInvalidUser = mockUser.copy(userId = UUID.fromString(" "))
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidUser)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test(expected = IllegalArgumentException::class)
    fun `post user endpoint should return 400 when userId is not UUID`() =
        configureTestApplication {
            val mockInvalidUser = mockUser.copy(userId = UUID.fromString("e58ed763-928c-bee9-fdbaaadc15f3"))
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidUser)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Invalid UUID format", response.bodyAsText())
        }

    @Test
    fun `post user endpoint should return 400 when username is empty`() =
        configureTestApplication {
            val mockInvalidUser = mockUser.copy(username = "")
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidUser)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Username must not be empty", response.bodyAsText())
        }

    @Test
    fun `post user endpoint should return 400 when username is whitespace`() =
        configureTestApplication {
            val mockInvalidUser = mockUser.copy(username = " ")
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidUser)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Username must not be blank", response.bodyAsText())
        }

    @Test
    fun `post user endpoint should return 400 when password is empty`() =
        configureTestApplication {
            val mockInvalidUser = mockUser.copy(password = "")
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidUser)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Password must not be empty", response.bodyAsText())
        }

    @Test
    fun `post user endpoint should return 400 when password is whitespace`() =
        configureTestApplication {
            val mockInvalidUser = mockUser.copy(password = " ")
            val response = testHttpClient().post("/v1/user") {
                contentType(ContentType.Application.Json)
                setBody(mockInvalidUser)
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
            assertEquals("Password must not be blank", response.bodyAsText())
        }
}
