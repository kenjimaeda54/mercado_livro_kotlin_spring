package com.mercadolivro.exception

class LoginRequestException(override  val message: String,val httpCode: Int): Exception() {
}