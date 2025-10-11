package dev.jcasas.money.app.user.domain

import dev.jcasas.money.app.budget.domain.BudgetItem

/**
 * Template-like configuration for a budget. This will be used as a foundation for user's budget.
 */
data class BudgetConfig(
    val items: List<BudgetItem>
)
