package com.todolist.utils

import com.todolist.plugins.configureRouting
import com.todolist.utils.authentication.JwtService
import com.todolist.utils.database.configureDatabase
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

fun ApplicationTestBuilder.testHttpClient() = createClient {
    install(ContentNegotiation) {
        json()
    }
}

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

    block()
}
