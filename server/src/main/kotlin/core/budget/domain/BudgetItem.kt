package dev.jcasas.money.app.budget.domain

import java.math.BigDecimal

data class BudgetItem(
    val name: String,
    val amount: BigDecimal,
    val description: String?
)