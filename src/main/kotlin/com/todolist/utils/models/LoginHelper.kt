package com.todolist.utils.models

import com.todolist.models.Login
import com.todolist.tables.Users
import com.todolist.utils.database.DatabaseFactory
import com.todolist.utils.validation.RequestValidationResult
import org.jetbrains.exposed.sql.selectAll
import org.mindrot.jbcrypt.BCrypt

fun validateLoginRequest(request: Login): RequestValidationResult = when {
    request.username.isEmpty() -> RequestValidationResult.Invalid("Username must not be empty")
    request.username.isBlank() -> RequestValidationResult.Invalid("Username must not be blank")
    request.password.isEmpty() -> RequestValidationResult.Invalid("Password must not be empty")
    request.password.isBlank() -> RequestValidationResult.Invalid("Password must not be blank")
    else -> RequestValidationResult.Valid
}

suspend fun areLoginCredentialsPresent(username: String, password: String): Boolean = DatabaseFactory.databaseQuery {
    val allUsernames = Users.selectAll().map { resultRowToUser(it).username }
    var usernameExists = false

    allUsernames.forEach {
        if (BCrypt.checkpw(username, it)) {
            usernameExists = true
        }
    }

    val allPasswords = Users.selectAll().map { resultRowToUser(it).password }
    var passwordExists = false

    allPasswords.forEach {
        if (BCrypt.checkpw(password, it)) {
            passwordExists = true
        }
    }

    val userIsPresent = usernameExists && passwordExists

    return@databaseQuery userIsPresent
}
