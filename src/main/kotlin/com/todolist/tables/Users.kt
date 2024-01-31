package com.todolist.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

@Suppress("MagicNumber")
object Users : Table("Users") {
    val userId: Column<UUID> = uuid("userId").uniqueIndex()
    val username: Column<String> = varchar("username", 255)
    val password: Column<String> = varchar("password", 255)
}
