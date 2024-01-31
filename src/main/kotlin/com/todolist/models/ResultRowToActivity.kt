package com.todolist.models

import com.todolist.tables.Activities
import org.jetbrains.exposed.sql.ResultRow

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
