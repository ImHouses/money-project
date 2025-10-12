package core.budget.api.responses

import com.google.gson.annotations.SerializedName

data class BudgetResponsePayload(
    @SerializedName("year") val year: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("items") val items: List<BudgetItemResponsePayload>
)

data class BudgetItemResponsePayload(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("remaining_amount") val remainingAmount: String,
    @SerializedName("transactions") val transactions: List<TransactionResponsePayload>
)

data class TransactionResponsePayload(
    @SerializedName("id") val id: Int,
    @SerializedName("day") val day: Int,
    @SerializedName("month") val month: Int,
    @SerializedName("year") val year: Int,
    @SerializedName("amount") val amount: String,
    @SerializedName("budget_item_id") val budgetItemId: Int,
    @SerializedName("note") val note: String?,
)
