package io.test.moneytransfer.misc

import io.test.moneytransfer.service.ClientService
import java.math.BigDecimal
import java.time.LocalDate

/**
 * This class can be used to fill data for testing purpose
 */
class DataInitializer {
    private val clientService = ClientService()

    fun initialize() {
        clientService.create(
            firstName = "Ivan",
            lastName = "Ivanov",
            birthDate = LocalDate.of(1980, 1, 1),
            email = "test1@test.com",
            initialBalance = BigDecimal.valueOf(207)
        )

        clientService.create(
            firstName = "Sergey",
            lastName = "Sergeyev",
            birthDate = LocalDate.of(1990, 3, 1),
            email = "test1@test.com",
            initialBalance = BigDecimal.valueOf(690)
        )
    }
}