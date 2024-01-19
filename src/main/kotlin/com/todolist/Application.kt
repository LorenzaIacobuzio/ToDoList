package com.todolist

import com.typesafe.config.ConfigFactory
import com.todolist.plugins.*
import getStatusRoute
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    routing {
        route("/v1") {
            getStatusRoute()
        }
    }
}

object DatabaseFactory {
    fun init() {
        val database =  Database.connect(
            url = "jdbc:postgresql://localhost:5432/ToDoListDB",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
        )

        transaction (database) {
            //SchemaUtils.create()
        }
    }
}