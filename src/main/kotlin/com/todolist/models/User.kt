@file:UseSerializers(KUuidSerializer::class)

package com.todolist.models

import com.todolist.utils.KUuidSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
data class User(
    val userId: UUID,
    val username: String,
    val password: String
)
