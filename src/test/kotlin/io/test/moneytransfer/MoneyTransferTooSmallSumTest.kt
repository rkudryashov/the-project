package io.test.moneytransfer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate


class MoneyTransferTooSmallSumTest : AbstractTest() {

    /**
     * Tests money transfers from two accounts to each other
     * For both accounts one thread per transfer is created. Each transfer amount is of 1 rouble.
     */
    @Test
    fun testTooSmallSum() {
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

        val transferAmount = BigDecimal.valueOf(0.99)

        val request = createTransferRequest(senderClient.id, receiverClient.id, transferAmount)

        httpClient.newCall(request).execute().also {
            assertEquals(400, it.code)
            assertEquals("Transferred sum is too small", String(it.body!!.bytes()))
        }
    }
}