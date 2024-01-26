package com.todolist.utils

fun isValidUUID(id: String): Boolean {
    val uuidPattern = Regex(pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    return uuidPattern.matches(id)
}
