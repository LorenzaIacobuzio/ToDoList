package com.todolist

import com.todolist.tables.Activities
import getStatusRoute
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import postActivityRoute

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    DatabaseFactory.init("ToDoListDB")
    routing {
        route("/v1") {
            getStatusRoute()
            postActivityRoute()
        }
    }
}

object DatabaseFactory {
    fun init(databaseName: String) {
        val database =  Database.connect(
            url = "jdbc:postgresql://localhost:5432/$databaseName",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
        )

        transaction (database) {
            createActivitiesTable()
        }
    }

    private fun createActivitiesTable() {
        SchemaUtils.drop(Activities)
        SchemaUtils.create(Activities)
    }

    suspend fun <T> databaseQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
