package io.test.moneytransfer.dto

import java.math.BigDecimal

class TransferRequestDto(
    val senderId: Long,
    val receiverId: Long,
    val amount: BigDecimal
)
