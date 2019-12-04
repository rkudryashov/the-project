package io.test.moneytransfer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.test.moneytransfer.dto.ClientCreateDto
import io.test.moneytransfer.dto.ClientDto
import io.test.moneytransfer.dto.TransferDto
import io.test.moneytransfer.dto.TransferRequestDto
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.TimeUnit

abstract class AbstractTest {

    protected val httpClient: OkHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    private val serverUrl = "http://127.0.0.1:8080"

    companion object {
        private lateinit var server: ApplicationEngine

        @JvmStatic
        @BeforeAll
        fun setUp() {
            server = embeddedServer(Netty, commandLineEnvironment(arrayOf()))
            server.start(wait = false)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            server.stop(1, 1, TimeUnit.SECONDS)
        }
    }

    protected fun createTransferThread(senderId: Long, receiverId: Long, amount: BigDecimal): Thread {
        return Thread {
            val request = createTransferRequest(senderId, receiverId, amount)
            httpClient.newCall(request).execute().also {
                assert(it.code == 200) { "HTTP response isn't OK" }
                val transferDtoResponse =
                    objectMapper.readValue(String(it.body!!.bytes()), object : TypeReference<TransferDto>() {})
                assertEquals(senderId, transferDtoResponse.sender.id)
                assertEquals(receiverId, transferDtoResponse.receiver.id)
                assertEquals(amount, transferDtoResponse.amount)
                it.close()
            }
        }
    }

    protected fun createTransferRequest(senderId: Long, receiverId: Long, amount: BigDecimal): Request {
        val requestDto = TransferRequestDto(
            senderId = senderId,
            receiverId = receiverId,
            amount = amount
        )

        val requestBody = objectMapper.writeValueAsString(requestDto)

        return Request.Builder()
            .url("$serverUrl/api/transfers")
            .post(requestBody.toRequestBody())
            .headers(Headers.headersOf("Content-type", "application/json"))
            .build()
    }

    protected fun createClient(
        firstName: String,
        lastName: String,
        birthDate: LocalDate,
        email: String,
        initialBalance: BigDecimal
    ): ClientDto {
        val dto = ClientCreateDto(firstName, lastName, birthDate, email, initialBalance)

        val requestBody = objectMapper.writeValueAsString(dto)

        val clientCreateRequest: Request = Request.Builder()
            .url("$serverUrl/api/clients")
            .post(requestBody.toRequestBody())
            .headers(Headers.headersOf("Content-type", "application/json"))
            .build()

        val clientCreateResponse = httpClient.newCall(clientCreateRequest).execute().also {
            assertTrue(it.isSuccessful) { "HTTP response isn't OK" }
        }.body!!.bytes()

        return objectMapper.readValue(clientCreateResponse, object : TypeReference<ClientDto>() {})
    }

    protected fun getClient(id: Long): ClientDto {
        val clientRequest: Request = Request.Builder()
            .url("$serverUrl/api/clients/$id")
            .get()
            .headers(Headers.headersOf("Content-type", "application/json"))
            .build()

        val clientResponse = httpClient.newCall(clientRequest).execute().also {
            assert(it.code == 200) { "HTTP response isn't OK" }
        }.body!!.bytes()

        return objectMapper.readValue(clientResponse, object : TypeReference<ClientDto>() {})
    }
}