package io.test.moneytransfer.model

import java.math.BigDecimal
import java.time.LocalDateTime

class Transfer(
    var id: Long = 0,
    val sender: Client,
    val receiver: Client,
    val amount: BigDecimal,
    val time: LocalDateTime
)