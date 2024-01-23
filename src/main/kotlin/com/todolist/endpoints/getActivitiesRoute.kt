package com.todolist.endpoints

import com.todolist.DatabaseFactory.databaseQuery
import com.todolist.models.Activity
import com.todolist.models.resultRowToActivity
import com.todolist.tables.Activities
import com.todolist.utils.RequestValidationResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select

fun Route.getActivitiesRoute() {
    get("/activities/{userId}") {
        val userId = requireNotNull(call.parameters["userId"])
        when (val result = validateRequest(userId)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            is RequestValidationResult.Valid -> {
                val activitiesByUserId = getActivitiesByUser(userId)
                call.respond(status = HttpStatusCode.OK, activitiesByUserId)
            }
        }
    }
}

private fun validateRequest(userId: String): RequestValidationResult = when {
    !isValidUUID(userId) -> RequestValidationResult.Invalid("Invalid user ID")
    else -> RequestValidationResult.Valid
}

private fun isValidUUID(id: String): Boolean {
    val uuidPattern = Regex(pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    return uuidPattern.matches(id)
}


suspend fun getActivitiesByUser(userId: String): List<Activity> = databaseQuery {
    Activities.select{Activities.userId eq userId}.map {resultRowToActivity(it) }
}