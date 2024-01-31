package com.todolist

import com.todolist.plugins.configureRouting
import com.todolist.plugins.configureSerialization
import com.todolist.plugins.configureSsl
import com.todolist.plugins.configureStatusPages
import com.todolist.utils.database.configureDatabase
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureStatusPages()
    configureSsl()
    configureSerialization()
    configureDatabase(environment.config, false)
    configureRouting()
}
