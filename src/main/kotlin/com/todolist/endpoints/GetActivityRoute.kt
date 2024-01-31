package com.todolist.endpoints

import com.todolist.utils.RequestValidationResult
import com.todolist.utils.getActivity
import com.todolist.utils.validateId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.util.UUID

fun Route.getActivityRoute() {
    get("/activity/{id}") {
        val id = requireNotNull(call.parameters["id"])
        when (val result = validateId(id)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            is RequestValidationResult.Valid -> {
                val activity = getActivity(UUID.fromString(id))
                call.respond(status = HttpStatusCode.OK, activity)
            }
        }
    }
}
