package com.github.kyamada.sample.model.exception

import io.ktor.http.*

abstract class SystemException(message: String, private val code: Int? = null, ex: Exception? = null) :
        RuntimeException(message, ex) {
    abstract val status: HttpStatusCode
    fun response() = HttpError(
            code = code ?: status.value,
            message = message ?: "error"
    )
}

class InternalServerErrorException : SystemException {
    constructor(message: String = "internal server error") : super(message, null)
    constructor(message: String, ex: Exception) : super(message, ex = ex)

    override val status: HttpStatusCode = HttpStatusCode.InternalServerError
}

class NotFoundException : SystemException {
    constructor(message: String = "not found") : super(message)

    override val status: HttpStatusCode = HttpStatusCode.NotFound
}

class BadRequestException : SystemException {
    constructor(message: String = "bad request") : super(message)

    override val status: HttpStatusCode = HttpStatusCode.BadRequest
}

data class HttpError(
        val message: String,
        val code: Int
)
