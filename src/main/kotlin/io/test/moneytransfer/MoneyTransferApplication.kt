package io.test.moneytransfer

import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.test.moneytransfer.misc.DataInitializer

fun main(args: Array<String>) {
    DataInitializer().initialize()
    val server = embeddedServer(Netty, commandLineEnvironment(args))
    server.start(wait = true)
}