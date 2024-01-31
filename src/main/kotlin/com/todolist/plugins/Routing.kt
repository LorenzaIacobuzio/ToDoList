package com.todolist.plugins

import com.todolist.endpoints.delete.deleteActivityRoute
import com.todolist.endpoints.get.getActivitiesRoute
import com.todolist.endpoints.get.getActivityRoute
import com.todolist.endpoints.get.getStatusRoute
import com.todolist.endpoints.post.postActivityRoute
import com.todolist.endpoints.post.postUserRoute
import com.todolist.endpoints.put.putActivityRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route("/v1") {
            getStatusRoute()
            getActivityRoute()
            getActivitiesRoute()
            postUserRoute()
            postActivityRoute()
            putActivityRoute()
            deleteActivityRoute()
        }
    }
}
