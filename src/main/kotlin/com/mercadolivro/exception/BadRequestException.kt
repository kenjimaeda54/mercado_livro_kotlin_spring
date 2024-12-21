package com.mercadolivro.exception

class BadRequestException(override val message: String, val code: String): Exception() {
}