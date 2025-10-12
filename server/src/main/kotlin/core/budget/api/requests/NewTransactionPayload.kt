package core.budget.api.requests

import com.google.gson.annotations.SerializedName

data class NewTransactionPayload(
    @SerializedName("amount") val amount: String,
    @SerializedName("budget_item_id") val budgetItemId: Int,
    @SerializedName("day") val day: Int,
    @SerializedName("note") val note: String
)
