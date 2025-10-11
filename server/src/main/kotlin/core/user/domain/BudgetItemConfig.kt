package core.user.domain

import java.math.BigDecimal

data class BudgetItemConfig(
    val name: String,
    val amount: BigDecimal,
    val description: String?
)