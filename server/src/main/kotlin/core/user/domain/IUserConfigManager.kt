package core.user.domain

interface IUserConfigManager {
    suspend fun getBudgetConfig(): BudgetConfig
}