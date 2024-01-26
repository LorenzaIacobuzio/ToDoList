package com.todolist.tables

import com.todolist.models.Frequency
import com.todolist.models.Priority
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

@Suppress("MagicNumber")
object Activities : UUIDTable("Activities", "id") {
    val userId: Column<UUID> = uuid("userId")
    val title: Column<String> = varchar("title", 255)
    val group: Column<String?> = varchar("group", 255).nullable()
    val dueDate: Column<Instant> = timestamp("dueDate")
    val priority: Column<Priority?> = enumerationByName<Priority>("priority", 255).nullable()
    val description: Column<String?> = varchar("description", 255).nullable()
    val rescheduledToDate: Column<Instant?> = timestamp("rescheduledToDate").nullable()
    val frequency: Column<Frequency> = enumerationByName<Frequency>("frequency", 255)
}
