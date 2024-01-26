package com.todolist.utils

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder

fun ApplicationTestBuilder.testHttpClient() = createClient {
    install(ContentNegotiation) {
        json()
    }
}
