package io.test.moneytransfer.dto

import java.math.BigDecimal
import java.util.*

class AccountDto(
    val balance: BigDecimal,
    val currency: Currency
)
