package core.budget.domain

import java.math.BigDecimal

data class BudgetItem(
    val id: Int,
    val name: String,
    val amount: BigDecimal,
    val remainingAmount: BigDecimal,
    val transactions: List<BudgetTransaction>,
    val description: String?
)