package io.test.moneytransfer.model

import java.math.BigDecimal
import java.util.*
import java.util.concurrent.locks.ReentrantLock

class Account(
    val lock: ReentrantLock = ReentrantLock(),
    var id: Long = 0,
    @Volatile
    var balance: BigDecimal = BigDecimal.ZERO,
    val currency: Currency = Currency.getInstance("RUB")
)