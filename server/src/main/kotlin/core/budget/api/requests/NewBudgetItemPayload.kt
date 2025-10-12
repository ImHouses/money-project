package core.budget.api.requests

import com.google.gson.annotations.SerializedName

data class NewBudgetItemPayload(
    @SerializedName("name") val name: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("description") val description: String?,
)