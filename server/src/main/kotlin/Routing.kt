import core.budget.api.budgetApi
import core.user.api.configApi
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(mapOf("message" to "Hello World!"))
        }
        route("api/v1") {
            configApi()
            budgetApi()
        }
    }
}
