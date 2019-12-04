package io.test.moneytransfer.dto

import java.math.BigDecimal
import java.time.LocalDateTime

class TransferDto(
    val id: Long,
    val sender: ClientDto,
    val receiver: ClientDto,
    val amount: BigDecimal,
    val time: LocalDateTime
)