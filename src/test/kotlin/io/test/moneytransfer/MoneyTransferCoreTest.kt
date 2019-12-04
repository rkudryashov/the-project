package io.test.moneytransfer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class MoneyTransferCoreTest : AbstractTest() {

    /**
     * Tests money transfers from two accounts to each other
     * For both accounts one thread per transfer is created. Each transfer amount is of 1 rouble.
     */
    @Test
    fun testCore() {
        var poorClient = createClient(
            "Ivan",
            "Ivanov",
            LocalDate.of(1980, 1, 1),
            "test1@test.com",
            BigDecimal.valueOf(207)
        )

        var richClient = createClient(
            "Sergey",
            "Sergeyev",
            LocalDate.of(1990, 3, 1),
            "test2@test.com",
            BigDecimal.valueOf(690)
        )

        // state before
        assertEquals(BigDecimal.valueOf(207), poorClient.account.balance)
        assertEquals(BigDecimal.valueOf(690), richClient.account.balance)

        val transferAmount = BigDecimal.ONE

        val poorClientTransfersCount = poorClient.account.balance.toLong() / transferAmount.toLong()
        val poorClientTransfersBunch: List<Thread> = (0 until poorClientTransfersCount).map {
            createTransferThread(poorClient.id, richClient.id, transferAmount)
        }

        val richClientTransfersCount = richClient.account.balance.toLong() / transferAmount.toLong()
        val richClientTransfersBunch: List<Thread> = (0 until richClientTransfersCount).map {
            createTransferThread(richClient.id, poorClient.id, transferAmount)
        }

        val transferThreads = poorClientTransfersBunch + richClientTransfersBunch

        transferThreads.forEach { it.start() }
        transferThreads.forEach { it.join() }

        // state after
        poorClient = getClient(poorClient.id)
        richClient = getClient(richClient.id)

        assertEquals(BigDecimal.valueOf(690), poorClient.account.balance)
        assertEquals(BigDecimal.valueOf(207), richClient.account.balance)
    }
}