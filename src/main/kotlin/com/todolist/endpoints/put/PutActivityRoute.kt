package com.todolist.endpoints.put

import com.todolist.models.Activity
import com.todolist.utils.models.updateActivity
import com.todolist.utils.models.validateActivityRequest
import com.todolist.utils.validation.RequestValidationResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.put

fun Route.putActivityRoute() {
    put("/activity") {
        val request = call.receive<Activity>()

        when (val result = validateActivityRequest(request)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            RequestValidationResult.Valid -> {
                updateActivity(request)
                call.respond(status = HttpStatusCode.Created, message = "Activity updated")
            }
        }
    }
}
