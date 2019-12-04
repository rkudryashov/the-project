package io.test.moneytransfer.dto

import java.math.BigDecimal
import java.time.LocalDate

class ClientCreateDto(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val email: String,
    val initialBalance: BigDecimal
)