package io.test.moneytransfer.dao

import io.test.moneytransfer.model.Client
import java.util.concurrent.atomic.AtomicLong

class ClientDao {

    companion object {
        private val idGenerator = AtomicLong()
    }

    fun save(client: Client): Client {
        val id = idGenerator.incrementAndGet()
        client.id = id
        client.account.id = id
        DataStorage.idToClient[client.id] = client
        return client
    }

    fun getAll(): List<Client> = DataStorage.idToClient.values.toList()

    fun get(id: Long): Client? = DataStorage.idToClient[id]
}