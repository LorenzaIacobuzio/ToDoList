@file:UseSerializers(KInstantSerializer::class, KUuidSerializer::class)

package com.todolist.models

import com.todolist.utils.serializers.KInstantSerializer
import com.todolist.utils.serializers.KUuidSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant
import java.util.UUID

@Serializable
data class Activity(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val activityGroup: String? = null,
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
