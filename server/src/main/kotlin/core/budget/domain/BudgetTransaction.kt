package core.budget.domain

import java.math.BigDecimal

data class BudgetTransaction(
    val id: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val amount: BigDecimal,
    val budgetItemId: Int,
    val note: String?
)
