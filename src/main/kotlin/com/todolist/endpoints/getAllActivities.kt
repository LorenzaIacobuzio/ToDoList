import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.getAllActivitiesRoute() {
    get("/activities/all") {
        call.response.status(HttpStatusCode.OK)
    }
}
