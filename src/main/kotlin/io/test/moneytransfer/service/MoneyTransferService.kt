package io.test.moneytransfer.service

import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import io.test.moneytransfer.model.Transfer
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.concurrent.locks.ReentrantLock

class MoneyTransferService {

    private val clientService = ClientService()
    private val transferService = TransferService()
    private val maxTransferSum = BigDecimal.valueOf(500000)
    private val minTransferSum = BigDecimal.valueOf(1)
    private val log = LoggerFactory.getLogger(this::class.java)

    @KtorExperimentalAPI
    fun transfer(senderId: Long, receiverId: Long, amount: BigDecimal): Transfer {
        log.info("Start money transfer. Params: senderId=$senderId, receiverId=$receiverId, amount=$amount.")

        if (senderId == receiverId) throw BadRequestException("Can't pass money to the same account")

        if (amount > maxTransferSum) throw BadRequestException("Transferred sum is too big")
        if (amount < minTransferSum) throw BadRequestException("Transferred sum is too small")

        val sender = clientService.get(senderId)
            ?: throw NotFoundException("Client with id=$senderId doesn't exist")
        val receiver = clientService.get(receiverId)
            ?: throw NotFoundException("Client with id=$receiverId doesn't exist")

        val senderAccount = sender.account
        val receiverAccount = receiver.account

        val locks: Pair<ReentrantLock, ReentrantLock> =
            listOf(senderAccount, receiverAccount)
                // sorting to avoid deadlocks
                .sortedBy { it.id }
                .map { it.lock }
                .zipWithNext()[0]

        val firstLock = locks.first
        val secondLock = locks.second

        firstLock.lock()
        secondLock.lock()

        try {
            log.info("Locks are acquired.")
            val isNotEnoughMoney = sender.account.balance - amount < BigDecimal.ZERO
            if (isNotEnoughMoney) throw BadRequestException("It is not enough money to make transfer")

            senderAccount.balance -= amount
            receiverAccount.balance += amount
            return transferService.create(sender, receiver, amount)
        } finally {
            firstLock.unlock()
            secondLock.unlock()
            log.info("Locks are released.")
            log.info("End money transfer.")
        }
    }
}