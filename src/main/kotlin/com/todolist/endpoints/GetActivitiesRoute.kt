package com.todolist.endpoints

import com.todolist.utils.RequestValidationResult
import com.todolist.utils.getActivities
import com.todolist.utils.validateId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.util.UUID

fun Route.getActivitiesRoute() {
    get("/activities/{userId}") {
        val userId = requireNotNull(call.parameters["userId"])
        when (val result = validateId(userId)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            is RequestValidationResult.Valid -> {
                val activitiesByUserId = getActivities(UUID.fromString(userId))
                call.respond(status = HttpStatusCode.OK, activitiesByUserId)
            }
        }
    }
}
