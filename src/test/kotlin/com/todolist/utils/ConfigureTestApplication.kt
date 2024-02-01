package com.todolist.utils

import com.todolist.models.Login
import com.todolist.models.User
import com.todolist.plugins.configureRouting
import com.todolist.utils.authentication.JwtService
import com.todolist.utils.database.configureDatabase
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
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
import java.util.UUID
import kotlin.test.assertEquals

var authenticationToken: String = ""
val testUser = User(
    userId = UUID.fromString("00000000-1111-2222-3333-444455556666"),
    username = "LorenzaTest",
    password = "lorenzatest"
)
val testLogin = Login(
    username = "LorenzaTest",
    password = "lorenzatest"
)

fun configureTestApplication(
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

    val userResponse = testHttpClient().post("/v1/user") {
        contentType(ContentType.Application.Json)
        setBody(testUser)
    }

    assertEquals(HttpStatusCode.Created, userResponse.status)

    val loginResponse = testHttpClient().post("/v1/login") {
        contentType(ContentType.Application.Json)
        setBody(testLogin)
    }

    assertEquals(HttpStatusCode.OK, loginResponse.status)
    authenticationToken = loginResponse.bodyAsText().removePrefix("{\"token\":\"").removeSuffix("\"}")

    block()
}

fun ApplicationTestBuilder.testHttpClient() = createClient {
    install(ContentNegotiation) {
        json()
    }
    install(Auth) {
        bearer {
            loadTokens {
                BearerTokens(authenticationToken, authenticationToken)
            }
        }
    }
}
