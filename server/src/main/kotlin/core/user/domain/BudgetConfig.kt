package core.user.domain

import core.budget.domain.BudgetItem

/**
 * Template-like configuration for a budget. This will be used as a foundation for user's budget.
 */
data class BudgetConfig(
    val items: List<BudgetItemConfig>
)
