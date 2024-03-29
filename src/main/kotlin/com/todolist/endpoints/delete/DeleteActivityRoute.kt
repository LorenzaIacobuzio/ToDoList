package com.todolist.endpoints.delete

import com.todolist.utils.models.deleteActivity
import com.todolist.utils.validation.RequestValidationResult
import com.todolist.utils.validation.validateId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import java.util.UUID

fun Route.deleteActivityRoute() {
    delete("/activity/{id}") {
        val id = requireNotNull(call.parameters["id"])
        when (val result = validateId(id)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            is RequestValidationResult.Valid -> {
                deleteActivity(UUID.fromString(id))
                call.respond(status = HttpStatusCode.OK, "Activity deleted")
            }
        }
    }
}
