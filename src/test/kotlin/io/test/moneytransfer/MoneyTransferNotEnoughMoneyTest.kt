package io.test.moneytransfer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate


class MoneyTransferNotEnoughMoneyTest : AbstractTest() {

    /**
     * Tests money transfers from two accounts to each other
     * For both accounts one thread per transfer is created. Each transfer amount is of 1 rouble.
     */
    @Test
    fun testNotEnoughMoney() {
        val senderClient = createClient(
            "Ivan",
            "Ivanov",
            LocalDate.of(1980, 1, 1),
            "test1@test.com",
            BigDecimal.valueOf(20)
        )

        val receiverClient = createClient(
            "Sergey",
            "Sergeyev",
            LocalDate.of(1990, 3, 1),
            "test2@test.com",
            BigDecimal.ZERO
        )

        val transferAmount = BigDecimal.ONE

        val senderClientTransfersCount = senderClient.account.balance.toLong() / transferAmount.toLong()
        val senderClientTransfersBunch: List<Thread> = (0 until senderClientTransfersCount).map {
            createTransferThread(senderClient.id, receiverClient.id, transferAmount)
        }

        senderClientTransfersBunch.forEach { it.start() }
        senderClientTransfersBunch.forEach { it.join() }

        // after `senderClient` have transferred all his money let's make sure the next transfer will end up with error

        val request = createTransferRequest(senderClient.id, receiverClient.id, transferAmount)

        httpClient.newCall(request).execute().also {
            assertEquals(400, it.code)
            assertEquals("It is not enough money to make transfer", String(it.body!!.bytes()))
        }
    }
}