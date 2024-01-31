package com.todolist.endpoints.post

import com.todolist.models.User
import com.todolist.utils.models.addNewUser
import com.todolist.utils.models.hashUserCredentials
import com.todolist.utils.models.isUserAlreadyPresent
import com.todolist.utils.models.validateUserRequest
import com.todolist.utils.validation.RequestValidationResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.postUserRoute() {
    post("/user") {
        val user = call.receive<User>()

        when (val result = validateUserRequest(user)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            RequestValidationResult.Valid -> {
                if (!isUserAlreadyPresent(user)) {
                    addNewUser(hashUserCredentials(user))
                    call.respond(status = HttpStatusCode.Created, message = "User created")
                } else {
                    call.respond(status = HttpStatusCode.Conflict, message = "User already exists")
                }
            }
        }
    }
}
