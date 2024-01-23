package com.todolist.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

internal fun toDoListTestApplication(
    block: suspend ApplicationTestBuilder.(HttpClient) -> Unit
) {
    testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }

        val testClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        block(testClient)
    }
}


fun ApplicationTestBuilder.testHttpClient() = createClient {
    install(ContentNegotiation) {
        json()
    }
}

