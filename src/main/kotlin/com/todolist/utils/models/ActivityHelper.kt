package com.todolist.utils.models

import com.todolist.models.Activity
import com.todolist.tables.Activities
import com.todolist.utils.database.DatabaseFactory
import com.todolist.utils.validation.RequestValidationResult
import com.todolist.utils.validation.isValidUUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.util.UUID

suspend fun getActivities(userId: UUID): List<Activity> = DatabaseFactory.databaseQuery {
    Activities.select { Activities.userId eq userId }.map { resultRowToActivity(it) }
}

suspend fun getActivity(id: UUID): Activity = DatabaseFactory.databaseQuery {
    Activities.select { Activities.id eq id }.map { resultRowToActivity(it) }.first()
}

suspend fun deleteActivity(id: UUID) = DatabaseFactory.databaseQuery {
    Activities.deleteWhere { Activities.id eq id }
}

fun validateActivityRequest(request: Activity): RequestValidationResult = when {
    request.title.isEmpty() -> RequestValidationResult.Invalid("Title must not be empty")
    request.title == " " -> RequestValidationResult.Invalid("Title must not be whitespace")
    !isValidUUID(request.userId.toString()) -> RequestValidationResult.Invalid("Invalid UUID format")
    !isValidUUID(request.id.toString()) -> RequestValidationResult.Invalid("Invalid UUID format")
    else -> RequestValidationResult.Valid
}

suspend fun addNewActivity(activity: Activity) = DatabaseFactory.databaseQuery {
    val insertStatement = Activities.insert {
        it[id] = activity.id
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

suspend fun updateActivity(activity: Activity) = DatabaseFactory.databaseQuery {
    Activities.update {
        it[id] = activity.id
        it[userId] = activity.userId
        it[title] = activity.title
        it[group] = activity.group
        it[dueDate] = activity.dueDate
        it[priority] = activity.priority
        it[description] = activity.description
        it[rescheduledToDate] = activity.rescheduledToDate
        it[frequency] = activity.frequency
    }
}

fun resultRowToActivity(row: ResultRow) = Activity(
    id = row[Activities.id],
    userId = row[Activities.userId],
    title = row[Activities.title],
    group = row[Activities.group],
    dueDate = row[Activities.dueDate],
    priority = row[Activities.priority],
    description = row[Activities.description],
    rescheduledToDate = row[Activities.rescheduledToDate],
    frequency = row[Activities.frequency]
)
