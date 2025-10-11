package core.budget.domain

import java.math.BigDecimal

data class BudgetTransaction(
    val id: Int,
    val amount: BigDecimal,
    val budgetItemId: Int,
    val note: String?
)
