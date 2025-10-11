package core.budget.api

import core.budget.app.BudgetManager
import dev.jcasas.money.common.api.failureResponse
import dev.jcasas.money.common.api.successResponse
import core.user.app.UserConfigManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.budgetApi() {
    val userConfigManager = UserConfigManager()
    val budgetManager = BudgetManager(userConfigManager)

    route("/budget") {
        get("/{year}/{month}") {
            val year = call.parameters["year"]?.toInt()
            val month = call.parameters["month"]?.toInt()
            if (year == null || month == null) {
                throw IllegalArgumentException("Invalid dates")
            }
            runCatching {
                val budget = budgetManager.get(year, month)

            }.onSuccess {
                call.respond(successResponse(it))
            }.onFailure {
                call.respond(HttpStatusCode.InternalServerError, failureResponse(it.message))
            }
        }
    }
}