package io.test.moneytransfer.misc

import io.test.moneytransfer.dto.AccountDto
import io.test.moneytransfer.dto.ClientDto
import io.test.moneytransfer.dto.TransferDto
import io.test.moneytransfer.model.Account
import io.test.moneytransfer.model.Client
import io.test.moneytransfer.model.Transfer

interface Converter<E, D> {
    fun toDto(entity: E): D
}

class ClientConverter : Converter<Client, ClientDto> {
    private val accountConverter = AccountConverter()

    override fun toDto(entity: Client): ClientDto = ClientDto(
        id = entity.id,
        firstName = entity.firstName,
        lastName = entity.lastName,
        birthDate = entity.birthDate,
        email = entity.email,
        account = accountConverter.toDto(entity.account)
    )
}

class AccountConverter : Converter<Account, AccountDto> {
    override fun toDto(entity: Account): AccountDto = AccountDto(
        balance = entity.balance,
        currency = entity.currency
    )
}

class TransferConverter : Converter<Transfer, TransferDto> {
    private val clientConverter = ClientConverter()

    override fun toDto(entity: Transfer): TransferDto = TransferDto(
        id = entity.id,
        sender = clientConverter.toDto(entity.sender),
        receiver = clientConverter.toDto(entity.receiver),
        amount = entity.amount,
        time = entity.time
    )
}