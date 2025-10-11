package dev.jcasas.money.core.user.db

import com.google.gson.Gson
import core.user.domain.BudgetItemConfig
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.json.json

object UserConfigDbModel : IntIdTable("user_configs") {
    val gson = Gson()

    val budgetItems = json(
        "budget_items",
        serialize = { budgetItems: List<BudgetItemConfig> ->
            gson.toJson(budgetItems)
        },
        deserialize = { json ->
            val jsonTree = gson.toJsonTree(json)
            if (jsonTree.isJsonArray) {
                jsonTree.asJsonArray.map {
                    BudgetItemConfig(
                        name = it.asString,
                        amount = it.asInt.toBigDecimal(),
                        description = it.asString
                    )
                }
            } else {
                emptyList()
            }
        }
    )
}