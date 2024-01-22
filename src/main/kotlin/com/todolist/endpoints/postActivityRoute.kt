import com.todolist.DatabaseFactory.databaseQuery
import com.todolist.models.Activity
import com.todolist.tables.Activities
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.util.UUID

fun Route.postActivityRoute() {
    post("/activity") {
        val request = call.receive<Activity>()
        addNewActivity(request)
        call.respond(status = HttpStatusCode.Created, request.userId)
    }
}

suspend fun addNewActivity(activity: Activity) = databaseQuery {
    val insertStatement = Activities.insert {
        it[userId] = activity.userId
        it[title] = activity.title
        it[group] = activity.group
        it[dueDate] = activity.dueDate
        it[priority] = activity.priority
        it[description] = activity.description
        it[rescheduledToDate] = activity.rescheduledToDate
        it[frequency] = activity.frequency
    }

    insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToActivity)
}

suspend fun getActivitiesByUser(userId: String): List<Activity> = databaseQuery {
    Activities.select{Activities.userId eq userId}.map { resultRowToActivity(it) }
}

private fun resultRowToActivity(row : ResultRow) = Activity(
    userId = row[Activities.userId],
    title = row[Activities.title],
    group = row[Activities.group],
    dueDate = row[Activities.dueDate],
    priority = row[Activities.priority],
    description = row[Activities.description],
    rescheduledToDate = row[Activities.rescheduledToDate],
    frequency = row[Activities.frequency]
)