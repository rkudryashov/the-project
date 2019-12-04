package io.test.moneytransfer.service

import io.test.moneytransfer.dao.ClientDao
import io.test.moneytransfer.model.Account
import io.test.moneytransfer.model.Client
import java.math.BigDecimal
import java.time.LocalDate

class ClientService {

    private val clientDao = ClientDao()

    fun get(id: Long): Client? = clientDao.get(id)

    fun getAll() = clientDao.getAll()

    // initialBalance parameter is used only for the testing purpose. There is ability to get rid of it by implementation
    // of `deposit` method. That goes out of the current task scope
    fun create(
        firstName: String,
        lastName: String,
        birthDate: LocalDate,
        email: String,
        initialBalance: BigDecimal
    ): Client {
        val client = Client(
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate,
            email = email,
            account = Account(balance = initialBalance)
        )

        return clientDao.save(client)
    }
}