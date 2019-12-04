package io.test.moneytransfer.dto

import java.time.LocalDate

class ClientDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val email: String,
    val account: AccountDto
)