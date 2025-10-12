package core.user.api.requests

import com.google.gson.annotations.SerializedName

data class ConfigPayload(
    @SerializedName("budget_items")
    val budgetItems: List<BudgetItemConfigPayload>
)

data class BudgetItemConfigPayload(
    @SerializedName("name")
    val name: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("description")
    val description: String?
)