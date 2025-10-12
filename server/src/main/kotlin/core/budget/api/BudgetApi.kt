package core.budget.api

import common.fromCents
import common.toCents
import core.budget.api.requests.NewBudgetItemPayload
import core.budget.api.requests.NewTransactionPayload
import core.budget.api.responses.BudgetItemResponsePayload
import core.budget.api.responses.BudgetResponsePayload
import core.budget.api.responses.TransactionResponsePayload
import core.budget.app.BudgetManager
import dev.jcasas.money.common.api.failureResponse
import dev.jcasas.money.common.api.successResponse
import core.user.app.UserConfigManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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
                val budget = newSuspendedTransaction {
                    budgetManager.get(year, month)
                }
                BudgetResponsePayload(
                    year = budget.year,
                    month = budget.month,
                    items = budget.items.map { item ->
                        BudgetItemResponsePayload(
                            id = item.id,
                            name = item.name,
                            amount = item.amount.fromCents().toString(),
                            remainingAmount = item.remainingAmount.fromCents().toString(),
                            transactions = item.transactions.map { transaction ->
                                TransactionResponsePayload(
                                    id = transaction.id,
                                    day = transaction.day,
                                    month = transaction.month,
                                    year = transaction.year,
                                    amount = transaction.amount.fromCents().toString(),
                                    budgetItemId = transaction.budgetItemId,
                                    note = transaction.note
                                )
                            }
                        )
                    }
                )
            }.onSuccess {
                call.respond(successResponse(it))
            }.onFailure {
                call.respond(HttpStatusCode.InternalServerError, failureResponse(it.message))
            }
        }

        post("{year}/{month}") {
            val year = call.parameters["year"]?.toInt()
            val month = call.parameters["month"]?.toInt()
            if (year == null || month == null) {
                throw IllegalArgumentException("Invalid dates")
            }
            val payload = call.receive<NewBudgetItemPayload>()
            runCatching {
                newSuspendedTransaction {
                    budgetManager.addBudgetItem(
                        year = year,
                        month = month,
                        name = payload.name,
                        amount = payload.amount.toCents(),
                        description = payload.description
                    )
                }
            }.onSuccess {
                call.respond(successResponse(it))
            }.onFailure {
                call.respond(HttpStatusCode.InternalServerError, failureResponse(it.message))
            }
        }

        post("/{year}/{month}/transactions") {
            val year = call.parameters["year"]?.toInt()
            val month = call.parameters["month"]?.toInt()
            if (year == null || month == null) {
                throw IllegalArgumentException("Invalid dates")
            }
            val payload = call.receive<NewTransactionPayload>()
            runCatching {
                newSuspendedTransaction {
                    budgetManager.recordTransaction(
                        budgetItemId = payload.budgetItemId,
                        amount = payload.amount.toCents(),
                        day = payload.day,
                        note = payload.note
                    )
                }
            }.onFailure {
                call.respond(HttpStatusCode.InternalServerError, failureResponse(it.message))
            }.onSuccess {
                call.respond(successResponse(it))
            }
        }
    }
}