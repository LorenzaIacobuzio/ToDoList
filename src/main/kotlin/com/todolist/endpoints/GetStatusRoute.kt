package com.todolist.endpoints

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.getStatusRoute() {
    get("/status") {
        call.response.status(HttpStatusCode.OK)
    }
}
