package core.user.app

import core.user.domain.BudgetConfig
import core.user.domain.BudgetItemConfig
import core.user.domain.IUserConfigManager
import dev.jcasas.money.core.user.db.UserConfigDbModel
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserConfigManager : IUserConfigManager {
    override suspend fun getBudgetConfig(): BudgetConfig = newSuspendedTransaction {
        UserConfigDbModel.selectAll()
            .firstOrNull()
            ?.let {
                BudgetConfig(items = it[UserConfigDbModel.budgetItems])
            } ?: throw IllegalStateException("No config.")
    }

    override suspend fun setBudgetConfig(configItems: List<BudgetItemConfig>) {
        newSuspendedTransaction {
            UserConfigDbModel.insert {
                it[UserConfigDbModel.budgetItems] = configItems
            }
        }
    }

    override suspend fun init(budgetItems: List<BudgetItemConfig>) {
        setBudgetConfig(budgetItems)
    }
}