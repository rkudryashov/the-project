package io.test.moneytransfer.service

import io.test.moneytransfer.dao.TransferDao
import io.test.moneytransfer.model.Client
import io.test.moneytransfer.model.Transfer
import java.math.BigDecimal
import java.time.LocalDateTime

class TransferService {

    private val transferDao = TransferDao()

    fun create(
        sender: Client,
        receiver: Client,
        amount: BigDecimal
    ) = transferDao.save(Transfer(sender = sender, receiver = receiver, amount = amount, time = LocalDateTime.now()))

    fun getAll(): List<Transfer> = transferDao.getAll()

    fun get(id: Long): Transfer? = transferDao.get(id)
}