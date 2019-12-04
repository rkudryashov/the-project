package io.test.moneytransfer.dao

import io.test.moneytransfer.model.Client
import io.test.moneytransfer.model.Transfer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Kind of database
 */
class DataStorage {
    companion object {
        val idToClient: MutableMap<Long, Client> = ConcurrentHashMap()
        val transfers: MutableList<Transfer> = CopyOnWriteArrayList()
    }
}