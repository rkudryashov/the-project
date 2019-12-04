package io.test.moneytransfer.module

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import io.test.moneytransfer.dto.ClientCreateDto
import io.test.moneytransfer.dto.ClientDto
import io.test.moneytransfer.dto.TransferDto
import io.test.moneytransfer.dto.TransferRequestDto
import io.test.moneytransfer.misc.ClientConverter
import io.test.moneytransfer.misc.TransferConverter
import io.test.moneytransfer.service.ClientService
import io.test.moneytransfer.service.MoneyTransferService
import io.test.moneytransfer.service.TransferService

@KtorExperimentalAPI
fun Application.module() {
    val moneyTransferService = MoneyTransferService()
    val clientService = ClientService()
    val clientConverter = ClientConverter()
    val transferService = TransferService()
    val transferConverter = TransferConverter()

    install(DefaultHeaders)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
        }
    }
    install(StatusPages) {
        exception<BadRequestException> { cause ->
            call.respondText(cause.message!!, ContentType.Application.Json, HttpStatusCode.BadRequest)
        }
    }

    routing {

        route("api") {

            route("clients") {
                post("/") {
                    val clientCreateDto = call.receive<ClientCreateDto>()
                    val client = clientService.create(
                        clientCreateDto.firstName,
                        clientCreateDto.lastName,
                        clientCreateDto.birthDate,
                        clientCreateDto.email,
                        clientCreateDto.initialBalance
                    )
                    call.respond(clientConverter.toDto(client))
                }

                get("/") {
                    val clientsDtos: List<ClientDto> = clientService.getAll().map { clientConverter.toDto(it) }
                    call.respond(clientsDtos)
                }

                get("{id}") {
                    val id = call.parameters["id"]!!.toLong()
                    val clientDto: ClientDto = clientService.get(id)?.let { clientConverter.toDto(it) }
                        ?: throw NotFoundException("Client with id=$id doesn't exist")
                    call.respond(clientDto)
                }
            }

            route("transfers") {
                post("/") {
                    val transferRequestDto = call.receive<TransferRequestDto>()
                    val storedTransfer = moneyTransferService.transfer(
                        transferRequestDto.senderId,
                        transferRequestDto.receiverId,
                        transferRequestDto.amount
                    )
                    call.respond(transferConverter.toDto(storedTransfer))
                }

                get("/") {
                    val transfersDtos: List<TransferDto> = transferService.getAll().map { transferConverter.toDto(it) }
                    call.respond(transfersDtos)
                }

                get("{id}") {
                    val id = call.parameters["id"]!!.toLong()
                    val transferDto: TransferDto =
                        transferService.get(id)?.let { transferConverter.toDto(it) }
                            ?: throw NotFoundException("Transfer with id=$id doesn't exist")
                    call.respond(transferDto)
                }
            }
        }
    }
}