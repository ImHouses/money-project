package core.user.domain

interface IUserConfigManager {
    suspend fun getBudgetConfig(): BudgetConfig
    suspend fun setBudgetConfig(configItems: List<BudgetItemConfig>)
    suspend fun init(budgetItems: List<BudgetItemConfig>)
}