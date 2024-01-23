@file:UseSerializers(KInstantSerializer::class)

package com.todolist.models

import com.todolist.utils.KInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

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
