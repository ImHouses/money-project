package core.budget.domain

import java.math.BigDecimal

interface IBudgetManager {
    suspend fun get(year: Int, month: Int): Budget
    suspend fun addBudgetItem(year: Int, month: Int, name: String, amount: BigDecimal, description: String?): BudgetItem
}