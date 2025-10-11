package core.user.api

import common.toCents
import core.user.domain.BudgetItemConfig
import dev.jcasas.money.core.user.api.requests.ConfigPayload
import core.user.app.UserConfigManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.configApi() {
    val configManager = UserConfigManager()

    route("/user/config") {
        install(RequestValidation) {
            validate<ConfigPayload> { configPayload ->
                configPayload.budgetItems.forEach { budgetItem ->
                    if (budgetItem.name.isEmpty()) {
                        return@validate ValidationResult.Invalid("Invalid payload")
                    }
                    if (budgetItem.description != null && budgetItem.description.isEmpty()) {
                        return@validate ValidationResult.Invalid("Invalid payload")
                    }
                    if (budgetItem.amount < 0) {
                        return@validate ValidationResult.Invalid("Invalid payload")
                    }
                }
                ValidationResult.Valid
            }
        }

        post {
            val configPayload = call.receive<ConfigPayload>()
            val budgetItems = configPayload.budgetItems.map {
                BudgetItemConfig(name = it.name, amount = it.amount.toCents(), description = it.description)
            }
            configManager.init(budgetItems)
            call.respond(HttpStatusCode.OK)
        }
    }
}