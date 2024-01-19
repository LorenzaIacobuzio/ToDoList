import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.*
import kotlin.test.*

class GetStatusRouteTest {
    @Test
    fun `status endpoint should return 200 when server is up and running`() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        val response = client.get("/v1/status")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}