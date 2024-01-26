package com.todolist.endpoints

import com.todolist.models.Activity
import com.todolist.models.resultRowToActivity
import com.todolist.tables.Activities
import com.todolist.utils.DatabaseFactory.databaseQuery
import com.todolist.utils.RequestValidationResult
import com.todolist.utils.isValidUUID
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.insert

fun Route.postActivityRoute() {
    post("/activity") {
        val request = call.receive<Activity>()

        when (val result = validateActivityRequest(request)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            RequestValidationResult.Valid -> {
                addNewActivity(request)
                call.respond(status = HttpStatusCode.Created, message = "Activity added to list")
            }
        }
    }
}

private fun validateActivityRequest(request: Activity): RequestValidationResult = when {
    request.title.isEmpty() -> RequestValidationResult.Invalid("Title must not be empty")
    request.title == " " -> RequestValidationResult.Invalid("Title must not be whitespace")
    !isValidUUID(request.userId.toString()) -> RequestValidationResult.Invalid("Invalid UUID format")
    else -> RequestValidationResult.Valid
}

suspend fun addNewActivity(activity: Activity) = databaseQuery {
    val insertStatement = Activities.insert {
        it[userId] = activity.userId
        it[title] = activity.title
        it[group] = activity.group
        it[dueDate] = activity.dueDate
        it[priority] = activity.priority
        it[description] = activity.description
        it[rescheduledToDate] = activity.rescheduledToDate
        it[frequency] = activity.frequency
    }

    insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToActivity)
}
