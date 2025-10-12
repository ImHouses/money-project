package core.budget.db

import org.jetbrains.exposed.dao.id.IntIdTable

object TransactionDbModel : IntIdTable("transactions") {
    val amount = integer("amount")
    val budgetItemId = integer("budget_item_id")
    val day = integer("day")
    val month = integer("month")
    val year = integer("year")
    val note = varchar("note", 255).nullable()
}