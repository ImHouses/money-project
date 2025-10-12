package core.budget.app

import core.budget.db.BudgetItemDbModel
import core.budget.db.TransactionDbModel
import core.budget.domain.Budget
import core.budget.domain.BudgetItem
import core.budget.domain.BudgetTransaction
import core.budget.domain.IBudgetManager
import core.user.domain.IUserConfigManager
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.math.BigDecimal

class BudgetManager(private val userConfigMgr: IUserConfigManager) : IBudgetManager {
    override suspend fun get(year: Int, month: Int): Budget {
        var budgetItems = BudgetItemDbModel.selectAll()
            .where { (BudgetItemDbModel.year eq year) and (BudgetItemDbModel.month eq month) }
            .map(::toBudgetItem)
        if (budgetItems.isEmpty()) {
            budgetItems = createBudgetItemsFromConfig(year, month)
        }
        if (budgetItems.isEmpty()) {
            throw IllegalStateException()
        }
        val budgetItemIds = budgetItems.map { it.id }
        val transactions = TransactionDbModel.selectAll()
            .where { (TransactionDbModel.budgetItemId) inList budgetItemIds }
            .map {
                BudgetTransaction(
                    id = it[TransactionDbModel.id].value,
                    day = it[TransactionDbModel.day],
                    month = it[TransactionDbModel.month],
                    year = it[TransactionDbModel.year],
                    amount = BigDecimal(it[TransactionDbModel.amount]),
                    budgetItemId = it[TransactionDbModel.budgetItemId],
                    note = it[TransactionDbModel.note]
                )
            }
        val transactionsByBudgetItemId = mutableMapOf<Int, MutableList<BudgetTransaction>>()
        transactions.forEach {
            val transactionsById = transactionsByBudgetItemId.getOrElse(it.budgetItemId) { mutableListOf() }
            transactionsById.add(it)
            if (it.budgetItemId !in transactionsByBudgetItemId) {
                transactionsByBudgetItemId[it.budgetItemId] = transactionsById
            }
        }
        val completeBudgetItems = budgetItems.map {
            val budgetTransactions = transactionsByBudgetItemId[it.id].orEmpty()
            it.copy(
                transactions = budgetTransactions,
                remainingAmount = it.amount - budgetTransactions.sumOf(BudgetTransaction::amount)
            )
        }
        return Budget(year = year, month = month, items = completeBudgetItems)
    }

    override suspend fun addBudgetItem(
        year: Int,
        month: Int,
        name: String,
        amount: BigDecimal,
        description: String?
    ): BudgetItem {
        val previouslySavedBudgetItemsCount = BudgetItemDbModel.selectAll()
            .where { (BudgetItemDbModel.year eq year) and (BudgetItemDbModel.month eq month) }
            .count()
        // We do this to prevent other functions not applying the templates.
        if (previouslySavedBudgetItemsCount <= 0) {
            createBudgetItemsFromConfig(year, month)
        }
        return BudgetItemDbModel.insert {
            it[BudgetItemDbModel.year] = year
            it[BudgetItemDbModel.month] = month
            it[BudgetItemDbModel.name] = name
            it[BudgetItemDbModel.amount] = amount.toInt()
            it[BudgetItemDbModel.description] = description
        }.resultedValues
            ?.firstOrNull()
            ?.let(::toBudgetItem) ?: throw IllegalStateException()
    }

    override suspend fun recordTransaction(
        budgetItemId: Int,
        amount: BigDecimal,
        day: Int,
        note: String?
    ): BudgetTransaction {
        val (month, year) = BudgetItemDbModel.select(BudgetItemDbModel.month, BudgetItemDbModel.year)
            .where { BudgetItemDbModel.id eq budgetItemId }
            .firstOrNull()
            ?.let {
                it[BudgetItemDbModel.month] to it[BudgetItemDbModel.year]
            } ?: throw IllegalStateException()
        return TransactionDbModel.insert {
            it[TransactionDbModel.amount] = amount.toInt()
            it[TransactionDbModel.budgetItemId] = budgetItemId
            it[TransactionDbModel.day] = day
            it[TransactionDbModel.month] = month
            it[TransactionDbModel.year] = year
            it[TransactionDbModel.note] = note
        }.resultedValues
            ?.firstOrNull()
            ?.let {
                BudgetTransaction(
                    id = it[TransactionDbModel.id].value,
                    day = it[TransactionDbModel.day],
                    month = it[TransactionDbModel.month],
                    year = it[TransactionDbModel.year],
                    amount = it[TransactionDbModel.amount].toBigDecimal(),
                    budgetItemId = it[TransactionDbModel.budgetItemId],
                    note = it[TransactionDbModel.note]
                )
            } ?: throw IllegalStateException()
    }

    private suspend fun createBudgetItemsFromConfig(year: Int, month: Int): List<BudgetItem> {
        val budgetTemplate = userConfigMgr.getBudgetConfig()
        return BudgetItemDbModel.batchInsert(budgetTemplate.items) { budgetItemConfig ->
            this[BudgetItemDbModel.year] = year
            this[BudgetItemDbModel.month] = month
            this[BudgetItemDbModel.name] = budgetItemConfig.name
            this[BudgetItemDbModel.amount] = budgetItemConfig.amount.toInt()
            this[BudgetItemDbModel.description] = budgetItemConfig.description
        }.map(::toBudgetItem)
    }

    private fun toBudgetItem(resultRow: ResultRow) = BudgetItem(
        id = resultRow[BudgetItemDbModel.id].value,
        name = resultRow[BudgetItemDbModel.name],
        amount = BigDecimal(resultRow[BudgetItemDbModel.amount]),
        description = resultRow[BudgetItemDbModel.description],
        remainingAmount = BigDecimal(0),
        transactions = emptyList()
    )
}