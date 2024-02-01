package com.todolist.utils.models

import com.todolist.models.User
import com.todolist.tables.Users
import com.todolist.utils.database.DatabaseFactory
import com.todolist.utils.validation.RequestValidationResult
import com.todolist.utils.validation.isValidUUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.mindrot.jbcrypt.BCrypt

suspend fun isUserAlreadyPresent(user: User): Boolean = DatabaseFactory.databaseQuery {
    val userIdExists = Users.select { Users.userId eq user.userId }.map { resultRowToUser(it) }.isNotEmpty()
    val allUsernames = Users.selectAll().map { resultRowToUser(it).username }
    var usernameExists = false

    allUsernames.forEach {
        if (BCrypt.checkpw(user.username, it)) {
            usernameExists = true
        }
    }

    val userIsPresent = userIdExists || usernameExists

    return@databaseQuery userIsPresent
}

suspend fun addNewUser(user: User) = DatabaseFactory.databaseQuery {
    val insertStatement = Users.insert {
        it[userId] = user.userId
        it[username] = user.username
        it[password] = user.password
    }

    insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
}

fun resultRowToUser(row: ResultRow) = User(
    userId = row[Users.userId],
    username = row[Users.username],
    password = row[Users.password]
)

fun validateUserRequest(request: User): RequestValidationResult = when {
    request.username.isEmpty() -> RequestValidationResult.Invalid("Username must not be empty")
    request.username.isBlank() -> RequestValidationResult.Invalid("Username must not be blank")
    request.password.isEmpty() -> RequestValidationResult.Invalid("Password must not be empty")
    request.password.isBlank() -> RequestValidationResult.Invalid("Password must not be blank")
    !isValidUUID(request.userId.toString()) -> RequestValidationResult.Invalid("Invalid UUID format")
    else -> RequestValidationResult.Valid
}

suspend fun getUsers(): List<User> = DatabaseFactory.databaseQuery {
    Users.selectAll().map { resultRowToUser(it) }
}
fun hashUserPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun hashUserUsername(username: String): String {
    return BCrypt.hashpw(username, BCrypt.gensalt())
}
