package io.test.moneytransfer.model

import java.time.LocalDate

class Client(
    var id: Long = 0,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val email: String,
    val account: Account
) {
    override fun toString(): String = "[id=$id, name=($firstName $lastName)]"
}