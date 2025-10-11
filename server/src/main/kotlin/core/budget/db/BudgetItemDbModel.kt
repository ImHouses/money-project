package core.budget.db

import org.jetbrains.exposed.dao.id.IntIdTable

object BudgetItemDbModel : IntIdTable("budget_items") {
    val year = integer("year").index("idx_year")
    val month = integer("month").index("idx_month")
    val name = varchar("name", 100)
    val amount = integer("amount")
    val description = varchar("description", 100).nullable()
}