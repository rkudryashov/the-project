package io.test.moneytransfer.dao

import io.test.moneytransfer.model.Transfer
import java.util.concurrent.atomic.AtomicLong

class TransferDao {
    companion object {
        private val idGenerator = AtomicLong()
    }

    fun save(transfer: Transfer): Transfer {
        DataStorage.transfers.add(transfer.apply {
            id = idGenerator.incrementAndGet()
        })
        return transfer
    }

    fun getAll(): List<Transfer> = DataStorage.transfers

    fun get(id: Long): Transfer? = DataStorage.transfers.find { it.id == id }
}