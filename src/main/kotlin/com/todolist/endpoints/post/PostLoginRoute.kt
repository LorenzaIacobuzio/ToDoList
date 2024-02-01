package com.todolist.endpoints.post

import com.todolist.models.Login
import com.todolist.utils.authentication.JwtService
import com.todolist.utils.models.validateLoginRequest
import com.todolist.utils.validation.RequestValidationResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.postLoginRoute(jwtService: JwtService) {
    post("/login") {
        val login = call.receive<Login>()

        when (val result = validateLoginRequest(login)) {
            is RequestValidationResult.Invalid -> call.respond(
                status = HttpStatusCode.BadRequest,
                message = result.errorMessage
            )

            RequestValidationResult.Valid -> {
                val token: String? = jwtService.createJwtToken(login)

                token?.let {
                    call.respond(hashMapOf("token" to token))
                } ?: call.respond(
                    message = HttpStatusCode.Unauthorized
                )
            }
        }
    }
}
