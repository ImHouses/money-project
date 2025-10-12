package common

import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.math.BigDecimal
import java.math.RoundingMode

private val A_HUNDRED: BigDecimal = BigDecimal(100)

fun Int.toCents(): BigDecimal = BigDecimal(this).multiply(A_HUNDRED)

fun String.toCents(): BigDecimal = BigDecimal(this).multiply(A_HUNDRED)

fun BigDecimal.toCents(): BigDecimal = this.multiply(A_HUNDRED)

fun BigDecimal.fromCents(): BigDecimal = this.divide(A_HUNDRED, 2, RoundingMode.HALF_EVEN)

fun validateAmount(amount: String): Boolean {
    var numberOfDecimalPoints = 0
    for (character in amount.toCharArray()) {
        if (numberOfDecimalPoints > 1) {
            return false
        }
        if (character == '.') {
            numberOfDecimalPoints++
            continue
        }
        if (!character.isDigit()) return false
    }
    if (numberOfDecimalPoints > 1) {
        return false
    }
    return true
}