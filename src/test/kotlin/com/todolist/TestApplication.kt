package com.todolist

import com.todolist.plugins.configureRouting
import com.todolist.plugins.configureSecurity
import com.todolist.plugins.configureSerialization
import com.todolist.plugins.configureSsl
import com.todolist.plugins.configureStatusPages
import com.todolist.utils.authentication.JwtService
import com.todolist.utils.database.configureDatabase
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val jwtService = JwtService(this)
    configureStatusPages()
    configureSsl()
    configureSecurity(jwtService)
    configureSerialization()
    configureDatabase(environment.config, true)
    configureRouting(jwtService)
}
