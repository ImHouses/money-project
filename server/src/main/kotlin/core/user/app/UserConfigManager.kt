package core.user.app

import core.user.domain.BudgetConfig
import core.user.domain.BudgetItemConfig
import core.user.domain.IUserConfigManager
import core.user.db.UserConfigDbModel
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class UserConfigManager : IUserConfigManager {
    override suspend fun getBudgetConfig(): BudgetConfig = UserConfigDbModel.selectAll()
        .firstOrNull()
        ?.let {
            BudgetConfig(items = it[UserConfigDbModel.budgetItems])
        } ?: throw IllegalStateException("No config.")

    override suspend fun setBudgetConfig(configItems: List<BudgetItemConfig>) {
        UserConfigDbModel.insert {
            it[UserConfigDbModel.budgetItems] = configItems
        }
    }

    override suspend fun init(budgetItems: List<BudgetItemConfig>) {
        setBudgetConfig(budgetItems)
    }
}