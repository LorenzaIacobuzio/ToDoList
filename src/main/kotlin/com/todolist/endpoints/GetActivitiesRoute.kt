package com.todolist.endpoints

import com.todolist.models.Activity
import com.todolist.models.resultRowToActivity
import com.todolist.tables.Activities
import com.todolist.utils.DatabaseFactory.databaseQuery
import com.todolist.utils.RequestValidationResult
import com.todolist.utils.isValidUUID
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.jetbrains.exposed.sql.select
import java.util.UUID

fun Route.getActivitiesRoute() {
    get("/activities/{userId}") {
        val userId = requireNotNull(call.parameters["userId"])
        when (val result = validateUserId(userId)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            is RequestValidationResult.Valid -> {
                val activitiesByUserId = getActivitiesByUser(UUID.fromString(userId))
                call.respond(status = HttpStatusCode.OK, activitiesByUserId)
            }
        }
    }
}

private fun validateUserId(userId: String): RequestValidationResult = when {
    !isValidUUID(userId) -> RequestValidationResult.Invalid("Invalid UUID format")
    else -> RequestValidationResult.Valid
}

suspend fun getActivitiesByUser(userId: UUID): List<Activity> = databaseQuery {
    Activities.select { Activities.userId eq userId }.map { resultRowToActivity(it) }
}
