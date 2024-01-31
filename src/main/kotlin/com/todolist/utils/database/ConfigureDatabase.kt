package com.todolist.utils.database

import io.ktor.server.config.ApplicationConfig

fun configureDatabase(config: ApplicationConfig, isTestDatabase: Boolean) {
    val database = DatabaseFactory.init(config)

    if (!isTestDatabase) {
        DatabaseFactory.createTables(database)
    } else {
        DatabaseFactory.dropTables(database)
        DatabaseFactory.createTables(database)
    }
}
