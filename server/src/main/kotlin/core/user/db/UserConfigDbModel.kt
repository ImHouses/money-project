package core.user.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import core.user.domain.BudgetItemConfig
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.json.json

object UserConfigDbModel : IntIdTable("user_configs") {
    private val gson = Gson()
    private val budgetItemConfigType = object : TypeToken<List<BudgetItemConfig>>() {}.type

    val budgetItems = json(
        "budget_items",
        serialize = { budgetItems: List<BudgetItemConfig> ->
            gson.toJson(budgetItems)
        },
        deserialize = { json ->
            gson.fromJson(json, budgetItemConfigType)
        }
    )
}