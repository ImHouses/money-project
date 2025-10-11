package core.budget.domain

/**
 * A monthly budget identified by month and year.
 */
data class Budget(
    val month: Int,
    val year: Int,
    val items: List<BudgetItem>
)
