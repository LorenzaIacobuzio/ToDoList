package com.todolist.plugins

import com.todolist.endpoints.deleteActivityRoute
import com.todolist.endpoints.getActivitiesRoute
import com.todolist.endpoints.getActivityRoute
import com.todolist.endpoints.getStatusRoute
import com.todolist.endpoints.postActivityRoute
import com.todolist.endpoints.putActivityRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route("/v1") {
            getStatusRoute()
            getActivityRoute()
            getActivitiesRoute()
            postActivityRoute()
            putActivityRoute()
            deleteActivityRoute()
        }
    }
}
