@file:UseSerializers(KInstantSerializer::class, KUuidSerializer::class)

package com.todolist.models

import com.todolist.utils.KInstantSerializer
import com.todolist.utils.KUuidSerializer
import java.sql.Date
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.serialization.*

@Serializable
data class Activity(
    val userId: String,
    val title: String,
    val group: String? = null,
    val dueDate: Instant,
    val priority: Priority? = Priority.MEDIUM,
    val description: String? = null,
    val rescheduledToDate: Instant? = null,
    val frequency: Frequency
)

@Serializable
enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}

@Serializable
enum class Frequency {
    DAILY,
    WEEKLY,
    BYWEEKLY,
    MONTHLY,
    ONCE
}