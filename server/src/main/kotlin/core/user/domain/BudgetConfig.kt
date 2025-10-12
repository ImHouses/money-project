package core.user.domain

/**
 * Template-like configuration for a budget. This will be used as a foundation for user's budget.
 */
data class BudgetConfig(
    val items: List<BudgetItemConfig>
)
