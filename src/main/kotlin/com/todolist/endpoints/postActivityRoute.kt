package com.todolist.endpoints

import com.todolist.DatabaseFactory.databaseQuery
import com.todolist.models.Activity
import com.todolist.models.resultRowToActivity
import com.todolist.tables.Activities
import com.todolist.utils.RequestValidationResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import java.util.UUID

fun Route.postActivityRoute() {
    post("/activity") {
        val request = call.receive<Activity>()

        when (val result = validateRequest(request)) {
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

private fun validateRequest(request: Activity): RequestValidationResult = when {
    request.userId.isEmpty() -> RequestValidationResult.Invalid("Username must not be empty")
    request.title.isEmpty() -> RequestValidationResult.Invalid("Title must not be empty")
    request.userId == " " -> RequestValidationResult.Invalid("Username must not be whitespace")
    request.title == " " -> RequestValidationResult.Invalid("Title must not be whitespace")
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

