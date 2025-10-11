package common

import java.math.BigDecimal
import java.math.RoundingMode

private val A_HUNDRED: BigDecimal = BigDecimal(100)

fun Int.toCents(): BigDecimal = BigDecimal(this).multiply(A_HUNDRED)

fun BigDecimal.toCents(): BigDecimal = this.multiply(A_HUNDRED)

fun BigDecimal.fromCents(): BigDecimal = this.divide(A_HUNDRED, 2, RoundingMode.HALF_EVEN)